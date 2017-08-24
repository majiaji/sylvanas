package com.fantasy.sylvanas.web;

import com.fantasy.sylvanas.client.domain.UserConfigDTO;
import com.fantasy.sylvanas.service.IUserConfigCenter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by jiaji on 2017/8/15.
 */
@Controller
public class UserConfigController {
    @Resource
    private IUserConfigCenter userConfigCenter;

    @RequestMapping("/getUserConfig")
    @ResponseBody
    public UserConfigDTO getUserConfig(String service, String scene) {
        return userConfigCenter.getByKey(service, scene);
    }
}
