package com.fantasy.sylvanas.client;

import com.alibaba.fastjson.JSON;
import com.fantasy.sylvanas.client.domain.FlumeData;
import com.fantasy.sylvanas.client.domain.UserConfigDO;
import com.fantasy.sylvanas.client.domain.UserConfigDTO;
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

    public UserConfigDTO getByKey(FlumeData flumeData) {
        HttpCallCenter.CallParam callParam = new HttpCallCenter.CallParam();
        Map<String, String> param = Maps.newHashMap();
        param.put("service", flumeData.getHeaders().getService());
        param.put("scene", flumeData.getHeaders().getScene());
        callParam.setBodyParam(param);
        String result = httpCallCenter.doCall("http://localhost:8081/getUserConfig", callParam, "get");
        return JSON.parseObject(result, UserConfigDTO.class);
    }
}
