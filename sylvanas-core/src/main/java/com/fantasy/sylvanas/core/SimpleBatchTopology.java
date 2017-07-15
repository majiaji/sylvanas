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
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.TopologyBuilder;
import com.alibaba.jstorm.batch.BatchTopologyBuilder;
import org.junit.Assert;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
public class SimpleBatchTopology {
    static boolean isLocal = true;
    static Config conf = JStormHelper.getConfig(null);
    static String topologyName;
    @Resource
    private SimpleSpout simpleSpout;

    public TopologyBuilder setBuilder() {
        BatchTopologyBuilder topologyBuilder = new BatchTopologyBuilder(topologyName);
        BoltDeclarer boltDeclarer = topologyBuilder.setSpout("Spout", simpleSpout, 1);
        topologyBuilder.setBolt("Bolt", new SimpleBolt(), 2).shuffleGrouping("Spout");
        return topologyBuilder.getTopologyBuilder();
    }

    @PostConstruct
    void init() {
        conf = new Config();
        isLocal = true;
        String[] className = Thread.currentThread().getStackTrace()[1].getClassName().split("\\.");
        topologyName = className[className.length - 1];

        try {
            JStormHelper.runTopology(setBuilder().createTopology(), topologyName, conf, 100,
                    new JStormHelper.CheckAckedFail(conf), isLocal);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed");
        }
    }
}
