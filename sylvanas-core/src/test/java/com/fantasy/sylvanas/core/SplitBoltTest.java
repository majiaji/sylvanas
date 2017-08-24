package com.fantasy.sylvanas.core;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.List;

/**
 * Created by jiaji on 2017/8/20.
 */
public class SplitBoltTest {
    @Test
    public void test() {
        String str = "2017-08-17 11:06:01,500|1502939161436|1502939161500|64|http://testd-ht.webank.com/app/s/wallet/face_lip_language|60841010|Code换取Token出错|null|";
        List<String> stringList = Splitter.on("|").splitToList(str);
        System.out.println(JSON.toJSONString(stringList));
    }

}