package com.fantasy.sylvanas.web;

import com.fantasy.sylvanas.service.MonitorCenter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by jiaji on 2017/7/17.
 */
@Controller
public class MonitorController {
    @Resource
    private MonitorCenter monitorCenter;

    @RequestMapping("/monitor")
    @ResponseBody
    public String monitor(String key) {
        return monitorCenter.getWordCount(key);
    }

    @RequestMapping("/show")
    public String show(Model model, String key) {
        model.addAttribute("key", key);
        return "show";
    }
}
