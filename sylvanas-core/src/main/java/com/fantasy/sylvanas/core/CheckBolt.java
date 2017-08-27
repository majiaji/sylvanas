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
import com.fantasy.sylvanas.client.RedisCenter;
import com.fantasy.sylvanas.client.domain.FlumeData;
import com.fantasy.sylvanas.client.domain.UserConfigDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * Created by jiaji on 2017/8/15.
 */
public class CheckBolt implements IBasicBolt, ICommitter {

    private static final Logger logger = LoggerFactory.getLogger(CheckBolt.class);


    private HttpUserConfigCenter httpUserConfigCenter;
    private RedisCenter redisCenter;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        logger.info("prepare checkBolt");
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext("spring-sylvanas-core-main.xml");
        httpUserConfigCenter = (HttpUserConfigCenter) applicationContext.getBean("httpUserConfigCenter");
        redisCenter = (RedisCenter) applicationContext.getBean("redisCenter");
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        try {
            Jedis jedis = redisCenter.getInstance();
            jedis.incr("check_num");
            jedis.close();
            FlumeData flumeData = JSON.parseObject(input.getString(1), FlumeData.class);
            if (flumeData == null || flumeData.getHeaders() == null || flumeData.getBody() == null) {
                logger.warn("非法的flumeData:{}", flumeData);
                return;
            }
            UserConfigDTO userConfigDTO = httpUserConfigCenter.getByKey(flumeData);
            if (userConfigDTO == null) {
                logger.warn("请先配置接入{}", flumeData);
                return;
            }
            collector.emit(new Values(input.getLong(0), input.getString(1)));
        } catch (Throwable t) {
            System.out.println("checkBolt 处理异常" + t.getMessage());
            logger.error(t.getMessage(), t);
        }
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("id", "data"));
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
