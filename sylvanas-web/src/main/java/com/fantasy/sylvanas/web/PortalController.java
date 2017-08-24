package com.fantasy.sylvanas.web;

import com.alibaba.fastjson.JSON;
import com.fantasy.sylvanas.client.domain.UserConfigDO;
import com.fantasy.sylvanas.client.domain.UserConfigDTO;
import com.fantasy.sylvanas.service.IUserConfigCenter;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by jiaji on 2017/8/20.
 */
@Controller
public class PortalController {
    @Resource
    private IUserConfigCenter userConfigCenter;

    @RequestMapping("/portal")
    String portal(String service, String scene, Model model) {
        if (StringUtils.isEmpty(service)) service = "weilidai";
        if (StringUtils.isEmpty(scene)) scene = "loan";
        UserConfigDTO userConfigDTO = userConfigCenter.getByKey(service, scene);
        model.addAttribute("service", service);
        model.addAttribute("scene", scene);
        model.addAttribute("userConfigDTO", JSON.toJSONString(userConfigDTO));
        return "portal";
    }

    @RequestMapping("/portal/updateConfig")
    @ResponseBody
    ApiResult<Boolean> updateConfig(UserConfigDO userConfigDO) {
        ApiResult<Boolean> apiResult = new ApiResult<>();
        Boolean result = userConfigCenter.updateConfig(userConfigDO);
        apiResult.setSuccess(true);
        apiResult.setResult(result);
        return apiResult;
    }
}
