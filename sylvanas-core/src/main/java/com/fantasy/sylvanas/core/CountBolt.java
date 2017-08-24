package com.fantasy.sylvanas.core;

/**
 * Created by jiaji on 2017/8/15.
 */

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.FailedException;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
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


public class CountBolt implements IBasicBolt, ICommitter {
    private static final Logger logger = LoggerFactory.getLogger(CountBolt.class);

    private HttpUserConfigCenter httpUserConfigCenter;
    private RedisCenter redisCenter;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        logger.info("prepare countBolt");
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext("spring-sylvanas-core-main.xml");
        httpUserConfigCenter = (HttpUserConfigCenter) applicationContext.getBean("httpUserConfigCenter");
        redisCenter = (RedisCenter) applicationContext.getBean("redisCenter");
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        FlumeData flumeData = JSON.parseObject(input.getString(1), FlumeData.class);
        UserConfigDTO userConfigDTO = httpUserConfigCenter.getByKey(flumeData);
        Jedis jedis = redisCenter.getInstance();
        if (jedis == null) {
            logger.error("获取jedis资源失败，统计失败");
            return;
        }
        String prefix = flumeData.getHeaders().getService() + "_" + flumeData.getHeaders().getScene() + "_";
        try {
            Map<String, String> paramMap = JSON.parseObject(input.getString(2), new TypeReference<Map<String, String>>() {
            });
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                jedis.incr(prefix + entry.getKey());
                jedis.incr(prefix + entry.getKey() + "_" + entry.getValue());
            }
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        } finally {
            jedis.close();
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
