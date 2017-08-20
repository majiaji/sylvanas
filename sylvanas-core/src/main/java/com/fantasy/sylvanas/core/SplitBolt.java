package com.fantasy.sylvanas.core;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.FailedException;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.alibaba.fastjson.JSON;
import com.alibaba.jstorm.batch.BatchId;
import com.alibaba.jstorm.batch.ICommitter;
import com.fantasy.sylvanas.client.HttpUserConfigCenter;
import com.fantasy.sylvanas.client.domain.FlumeData;
import com.fantasy.sylvanas.client.domain.UserConfigDO;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import shade.storm.com.google.common.base.Splitter;

import java.util.List;
import java.util.Map;

/**
 * Created by jiaji on 2017/8/15.
 */
public class SplitBolt implements IBasicBolt, ICommitter {

    private static final Logger logger = LoggerFactory.getLogger(SplitBolt.class);

    private HttpUserConfigCenter httpUserConfigCenter;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        logger.info("prepare splitBolt");
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext("spring-sylvanas-core-main.xml");
        httpUserConfigCenter = (HttpUserConfigCenter) applicationContext.getBean("httpUserConfigCenter");
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        try {
            System.out.println("split: " + input.getLong(0) + "  " + input.getString(1));
            FlumeData flumeData = JSON.parseObject(input.getString(1), FlumeData.class);
            UserConfigDO userConfigDO = httpUserConfigCenter.getByKey(flumeData);
            //获取切分的逻辑
            String splitRule = (String) userConfigDO.getConfigMap().get("split");
            if (StringUtils.isEmpty(splitRule)) {
                splitRule = "default";
            }
            Map<String, String> paramMap = null;
            switch (splitRule) {
                case "default": {
                    List<String> splitResult = Splitter.on("|").splitToList(flumeData.getBody());
                    paramMap = Maps.newLinkedHashMap();
                    for (int i = 0; i < splitResult.size(); i++) {
                        paramMap.put("param_" + i, splitResult.get(i));
                    }
                    break;
                }
                default: {

                }
            }
            collector.emit(new Values(input.getLong(0), input.getString(1), JSON.toJSONString(paramMap)));
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        }
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("id", "data", "paramMap"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    @Override
    public byte[] commit(BatchId id) throws FailedException {
        return new byte[0];
    }

    @Override
    public void revert(BatchId id, byte[] commitResult) {

    }
}
