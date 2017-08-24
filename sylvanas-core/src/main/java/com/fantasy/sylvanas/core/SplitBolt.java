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
import com.fantasy.sylvanas.client.domain.LogField;
import com.fantasy.sylvanas.client.domain.UserConfigDTO;
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
            UserConfigDTO userConfigDTO = httpUserConfigCenter.getByKey(flumeData);
            //切分的逻辑
            String splitRule = (String) userConfigDTO.getConfigMap().get("splitRule");
            if (StringUtils.isEmpty(splitRule)) {
                splitRule = "default";
            }
            List<String> splitResult;
            try {
                switch (splitRule) {
                    case "default": {
                        splitResult = Splitter.on("|").splitToList(flumeData.getBody());
                        break;
                    }
                    default: {
                        splitResult = Splitter.on("|").splitToList(flumeData.getBody());
                        break;
                    }
                }
            } catch (Exception e) {
                logger.error("切分异常 flumeData:{}", flumeData);
                return;
            }
            if (splitResult.size() == 0) {
                logger.error("切分结果为空,请检查格式 flumeData:{}", flumeData);
                return;
            }
            //收集结果
            Map<String, String> paramMap = Maps.newHashMap();
            List<LogField> logFieldRule = JSON.parseArray(JSON.toJSONString(userConfigDTO.getConfigMap().get("logFieldRule")), LogField.class);
            try {
                //默认全取
                if (logFieldRule == null) {
                    for (int i = 0; i < splitResult.size(); i++) {
                        paramMap.put("param_" + i, splitResult.get(i));
                    }
                }
                //按配置来
                else {
                    for (int i = 0; i < logFieldRule.size(); i++) {
                        LogField logField = logFieldRule.get(i);
                        if (logField.getNeed()) {
                            paramMap.put(StringUtils.isEmpty(logField.getName()) ? "param_" + i : logField.getName(), splitResult.get(logField.getIndex()));
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("提取异常 flumeData:{}", flumeData);
                return;
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
