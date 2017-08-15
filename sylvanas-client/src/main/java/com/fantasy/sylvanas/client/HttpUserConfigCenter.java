package com.fantasy.sylvanas.client;

import com.alibaba.fastjson.JSON;
import com.fantasy.sylvanas.client.domain.UserConfigDO;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by jiaji on 2017/8/15.
 */
public class HttpUserConfigCenter {

    private HttpCallCenter httpCallCenter;

    public HttpCallCenter getHttpCallCenter() {
        return httpCallCenter;
    }

    public void setHttpCallCenter(HttpCallCenter httpCallCenter) {
        this.httpCallCenter = httpCallCenter;
    }

    UserConfigDO getByKey(String service, String scene) {
        HttpCallCenter.CallParam callParam = new HttpCallCenter.CallParam();
        Map<String, String> param = Maps.newHashMap();
        param.put("service", service);
        param.put("scene", scene);
        callParam.setBodyParam(param);
        String result = httpCallCenter.doCall("http://localhost:8080/getUserConfig", callParam, "get");
        if (result == null) {
            throw new RuntimeException("获取设置失败");
        }
        return JSON.parseObject(result, UserConfigDO.class);
    }
}
