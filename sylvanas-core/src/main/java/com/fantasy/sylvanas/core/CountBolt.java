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
import backtype.storm.tuple.Values;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.jstorm.batch.BatchId;
import com.alibaba.jstorm.batch.ICommitter;
import com.fantasy.sylvanas.client.HttpUserConfigCenter;
import com.fantasy.sylvanas.client.RedisCenter;
import com.fantasy.sylvanas.client.domain.CountRule;
import com.fantasy.sylvanas.client.domain.FlumeData;
import com.fantasy.sylvanas.client.domain.UserConfigDTO;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
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
        jedis.incr("count_num");
        Map<String, String> paramMap = JSON.parseObject(input.getString(2), new TypeReference<Map<String, String>>() {
        });
        Map<String, Long> storeMap = Maps.newHashMap();
        if (paramMap == null) {
            logger.error("获取切分结果失败");
            return;
        }
        try {
            List<CountRule> fieldCountRuleList = JSON.parseArray(JSON.toJSONString(userConfigDTO.getConfigMap().get("fieldCountRule")), CountRule.class);
            for (CountRule countRule : fieldCountRuleList) {
                //condition filter
                if (!conditionFilter(countRule, paramMap)) {
                    continue;
                }
                String countKey = getCountKey(countRule, paramMap);
                switch (countRule.getRule()) {
                    case CountRule.Rule.SUM: {
                        storeMap.put(countKey, jedis.incr(countKey));
                        break;
                    }
                    case CountRule.Rule.AVG: {
                        //性能担忧
                        Transaction tx = jedis.multi();
                        tx.incr(countKey + "num");
                        tx.incrBy(countKey + "total", Long.valueOf(paramMap.get(countRule.getField())));
                        tx.exec();
                        String avg = String.valueOf(Long.valueOf(jedis.get(countKey + "total")) / Long.valueOf(jedis.get(countKey + "num")));
                        storeMap.put(countKey, Long.valueOf(avg));
                        break;
                    }
                    case CountRule.Rule.MAX: {
                        if (jedis.get(countKey) == null || Long.valueOf(jedis.get(countKey)) < Long.valueOf(paramMap.get(countRule.getField()))) {
                            jedis.set(countKey, paramMap.get(countRule.getField()));
                        }
                        break;
                    }
                    case CountRule.Rule.MIN: {
                        if (jedis.get(countKey) == null || Long.valueOf(jedis.get(countKey)) > Long.valueOf(paramMap.get(countRule.getField()))) {
                            jedis.set(countKey, paramMap.get(countRule.getField()));
                        }
                        break;
                    }
                    default: {
                        //do nothing
                    }
                }
            }
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        } finally {
            jedis.close();
        }
        collector.emit(new Values(input.getLong(0), input.getString(1), JSON.toJSONString(storeMap)));
    }

    private Boolean conditionFilter(CountRule countRule, Map<String, String> paramMap) {
        //filter不符合条件的数据
        if (countRule.getConditionMap() == null) {
            return true;
        }
        try {
            for (Map.Entry<String, String> entry : countRule.getConditionMap().entrySet()) {
                if (!paramMap.get(entry.getKey()).equals(entry.getValue())) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    String getCountKey(CountRule countRule, Map<String, String> paramMap) {
        StringBuilder sb = new StringBuilder();
        sb.append(countRule.getRule()).append("_");
        if (countRule.getField() != null) {
            sb.append("field:").append(countRule.getField()).append("_");
        }
        if (countRule.getGroupBy() != null) {
            sb.append("groupBy:").append(countRule.getGroupBy()).append("=").append(paramMap.get(countRule.getGroupBy())).append("_");
        }
        return sb.toString();
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("id", "data", "storeMap"));
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
