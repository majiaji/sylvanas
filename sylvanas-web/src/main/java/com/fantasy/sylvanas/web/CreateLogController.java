package com.fantasy.sylvanas.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jiaji on 2017/8/13.
 */

@Controller
public class CreateLogController {
    public static final Logger logger = LoggerFactory.getLogger(CreateLogController.class);

    @RequestMapping("/create/log")
    @ResponseBody
    public String createLog(String str) {
        logger.error(str);
        return str + " done!";
    }
}
