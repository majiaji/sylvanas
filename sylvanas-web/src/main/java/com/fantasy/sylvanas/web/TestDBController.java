package com.fantasy.sylvanas.web;

import com.fantasy.sylvanas.client.domain.UserConfigDO;
import com.fantasy.sylvanas.service.dao.IUserConfigDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jiaji on 2017/8/14.
 */
@Controller
public class TestDBController {

    @Resource
    IUserConfigDAO userConfigDAO;

    @RequestMapping("/test/db")
    @ResponseBody

    public List<UserConfigDO> testDB() {
        return userConfigDAO.listAll();
    }
}
