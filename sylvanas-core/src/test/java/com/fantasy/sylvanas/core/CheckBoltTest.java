package com.fantasy.sylvanas.core;

import com.alibaba.fastjson.JSON;
import com.fantasy.sylvanas.client.domain.FlumeData;
import org.junit.Test;

/**
 * Created by jiaji on 2017/8/16.
 */
public class CheckBoltTest {
    @Test
    public void test() {
        String str = "{\"headers\":{\"timestamp\":\"1502855728687\"},\"body\":\"2017-08-16 11:55:28,686 ERROR web.CreateLogController - 1234\"}";
        FlumeData flumeData = JSON.parseObject(str, FlumeData.class);
        System.out.println();
    }

}