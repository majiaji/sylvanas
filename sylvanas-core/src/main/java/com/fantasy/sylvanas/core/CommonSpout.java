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

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.FailedException;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import com.alibaba.jstorm.batch.BatchId;
import com.alibaba.jstorm.batch.IBatchSpout;
import com.fantasy.sylvanas.client.HttpUserConfigCenter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import shade.storm.com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class CommonSpout implements IBatchSpout {
    private static final Logger logger = LoggerFactory.getLogger(CommonSpout.class);
    KafkaConsumer<String, String> kafkaConsumer;
    private List<String> topicList;
    private Properties props;
    private Long pollTimeout = 1000L;
    private Random idGenerate;


    private HttpUserConfigCenter httpUserConfigCenter;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext("spring-sylvanas-core-main.xml");
        httpUserConfigCenter = (HttpUserConfigCenter) applicationContext.getBean("httpUserConfigCenter");
        logger.error("prepare CommonSpout");
        topicList = Lists.newLinkedList();
        topicList.add("sylvanas");
        props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "sylvanas-group");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(topicList);

        idGenerate = new Random(Utils.secureRandomLong());

    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        logger.error("CommonSpout start Execute");
        try {
            while (true) {
                try {
                    ConsumerRecords<String, String> records = kafkaConsumer.poll(pollTimeout);
                    for (ConsumerRecord<String, String> record : records) {
                        if (record.value() != null) {
                            collector.emit(new Values(idGenerate.nextLong(), record.value()));
                        }
                    }
                } catch (Throwable t) {
                    logger.error(t.getMessage(), t);
                }
            }
        } finally {
            kafkaConsumer.close();
        }
    }

    @Override
    public void cleanup() {
        if (kafkaConsumer != null) {
            kafkaConsumer.close();
        }
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
        logger.info("Receive BatchId " + id);
        return null;
    }

    @Override
    public void revert(BatchId id, byte[] commitResult) {
        logger.info("Receive BatchId " + id);
    }

}
