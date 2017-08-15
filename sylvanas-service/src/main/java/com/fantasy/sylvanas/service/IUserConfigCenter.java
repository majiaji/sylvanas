package com.fantasy.sylvanas.service;

import com.fantasy.sylvanas.client.domain.UserConfigDO;

/**
 * Created by jiaji on 2017/8/15.
 */
public interface IUserConfigCenter {
    UserConfigDO getByKey(String service, String scene);
}
