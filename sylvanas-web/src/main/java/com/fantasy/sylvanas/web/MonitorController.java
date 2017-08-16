package com.fantasy.sylvanas.web;

import com.fantasy.sylvanas.service.MonitorCenter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by jiaji on 2017/7/17.
 */
@Controller
public class MonitorController {
    @Resource
    private MonitorCenter monitorCenter;

    @RequestMapping("/monitor")
    @ResponseBody
    public Map monitor() {
        return monitorCenter.getWordCount("wordCount");
    }
}
