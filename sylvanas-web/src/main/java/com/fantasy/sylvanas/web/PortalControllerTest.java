package com.fantasy.sylvanas.web;

import com.alibaba.fastjson.JSON;
import com.fantasy.sylvanas.client.domain.LogField;
import com.fantasy.sylvanas.client.domain.UserConfigDO;
import com.fantasy.sylvanas.client.domain.UserConfigDTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by jiaji on 2017/8/20.
 */
public class PortalControllerTest {
    @Test
    public void test() {
        UserConfigDO userConfigDO = new UserConfigDO();
        userConfigDO.setId(123L);
        Map<String, Object> config = Maps.newHashMap();
        config.put("splitRule", "default");
        List<LogField> logFieldList = Lists.newLinkedList();
        LogField logField = new LogField();
        logField.setName("错误码");
        logField.setNeed(true);
        logFieldList.add(logField);
        config.put("logFieldRule", logFieldList);
        userConfigDO.setConfig(JSON.toJSONString(config));
        UserConfigDTO userConfigDTO = UserConfigDO.convertToDTO(userConfigDO);
        System.out.println(JSON.toJSONString(userConfigDTO));
    }
}