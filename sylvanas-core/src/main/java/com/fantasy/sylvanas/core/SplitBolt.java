package com.fantasy.sylvanas.core;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.FailedException;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.alibaba.jstorm.batch.BatchId;
import com.alibaba.jstorm.batch.ICommitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.Map;

/**
 * Created by jiaji on 2017/8/15.
 */
public class SplitBolt implements IBasicBolt, ICommitter {

    private static final Logger logger = LoggerFactory.getLogger(SplitBolt.class);


    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        logger.info("prepare splitBolt");
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext("spring-sylvanas-core-main.xml");
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        try {
            System.out.println("split: " + input.getLong(0) + "  " + input.getString(1));


            collector.emit(new Values(input.getLong(0), input.getString(1)));
        } catch (Throwable t) {
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
