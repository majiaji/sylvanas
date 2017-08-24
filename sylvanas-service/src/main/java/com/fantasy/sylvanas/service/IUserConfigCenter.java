package com.fantasy.sylvanas.service;

import com.fantasy.sylvanas.client.domain.UserConfigDO;
import com.fantasy.sylvanas.client.domain.UserConfigDTO;

/**
 * Created by jiaji on 2017/8/15.
 */
public interface IUserConfigCenter {
    UserConfigDTO getByKey(String service, String scene);

    Boolean updateConfig(UserConfigDO userConfigDO);
}
