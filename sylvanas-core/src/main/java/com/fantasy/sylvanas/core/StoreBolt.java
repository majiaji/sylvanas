/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fantasy.sylvanas.core;

import backtype.storm.Config;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.FailedException;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.utils.Utils;
import com.alibaba.jstorm.batch.BatchId;
import com.alibaba.jstorm.batch.ICommitter;
import com.alibaba.jstorm.utils.JStormUtils;
import com.alibaba.jstorm.utils.TimeCacheMap;
import com.fantasy.sylvanas.client.RedisCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import redis.clients.jedis.Jedis;
import shade.storm.com.google.common.base.Splitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class StoreBolt implements IBasicBolt, ICommitter {
    public static final String COUNT_BOLT_NAME = "Count";
    public static final String SUM_BOLT_NAME = "Sum";
    private static final long serialVersionUID = 5720810158625748042L;
    private static final Logger logger = LoggerFactory.getLogger(StoreBolt.class);
    private Map conf;

    private TimeCacheMap<BatchId, AtomicLong> counters;
    private BatchId currentId;

    private RedisCenter redisCenter;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        this.conf = stormConf;
        int timeoutSeconds = JStormUtils.parseInt(conf.get(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS), 30);
        counters = new TimeCacheMap<>(timeoutSeconds);
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext("spring-sylvanas-core-main.xml");
        redisCenter = (RedisCenter) applicationContext.getBean("redisCenter");
        logger.info("Successfully do prepare");
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String value = input.getString(0);
        List<String> wordList = Splitter.on(" ").splitToList(value.trim());
        Jedis jedis = redisCenter.getInstance();
        for (String each : wordList) {
            if (!each.equals("")) {
                jedis.hincrBy("wordCount", each, 1);
            }
        }
        jedis.close();
        System.out.println(value);
    }

    @Override
    public void cleanup() {
        logger.info("Successfully do cleanup");
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("counters"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    @Override
    public byte[] commit(BatchId id) throws FailedException {
        logger.info("Receive BatchId " + id);
        if (currentId == null) {
            currentId = id;
        } else if (currentId.getId() >= id.getId()) {
            logger.info("Current BatchId is " + currentId + ", receive:" + id);
            throw new RuntimeException();
        }
        currentId = id;

        AtomicLong counter = (AtomicLong) counters.remove(id);
        if (counter == null) {
            counter = new AtomicLong(0);
        }

        logger.info("Flush " + id + "," + counter);
        return Utils.serialize(id);
    }

    @Override
    public void revert(BatchId id, byte[] commitResult) {
        logger.info("Receive BatchId " + id);

        BatchId failedId = (BatchId) Utils.javaDeserialize(commitResult);

        if (!failedId.equals(id)) {
            logger.info("Deserialized error  " + id);
        }
    }
}
