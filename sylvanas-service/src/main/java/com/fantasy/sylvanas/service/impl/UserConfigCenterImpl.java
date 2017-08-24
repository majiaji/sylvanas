package com.fantasy.sylvanas.service.impl;

import com.fantasy.sylvanas.client.domain.UserConfigDO;
import com.fantasy.sylvanas.client.domain.UserConfigDTO;
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
    public UserConfigDTO getByKey(String service, String scene) {
        UserConfigDO userConfigDO = userConfigDAO.getByServiceAndScene(service, scene);
        return UserConfigDO.convertToDTO(userConfigDO);
    }

    @Override
    public Boolean updateConfig(UserConfigDO userConfigDO) {
        return userConfigDAO.uodateConfig(userConfigDO.getId(), userConfigDO.getConfig()) == 1;
    }
}
