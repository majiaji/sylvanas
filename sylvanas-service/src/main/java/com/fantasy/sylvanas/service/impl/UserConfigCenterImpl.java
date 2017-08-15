package com.fantasy.sylvanas.service.impl;

import com.fantasy.sylvanas.client.domain.UserConfigDO;
import com.fantasy.sylvanas.service.IUserConfigCenter;
import com.fantasy.sylvanas.service.dao.IUserConfigDAO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by jiaji on 2017/8/15.
 */
@Service
public class UserConfigCenterImpl implements IUserConfigCenter {

    @Resource
    private IUserConfigDAO userConfigDAO;

    @Override
    public UserConfigDO getByKey(String service, String scene) {
        return userConfigDAO.getByServiceAndScene(service, scene);
    }
}
