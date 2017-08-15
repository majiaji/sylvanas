package com.fantasy.sylvanas.service.dao;

import com.fantasy.sylvanas.service.domain.UserConfigDO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jiaji on 2017/8/14.
 */
@Repository
public interface UserConfigDAO {
    @Select("select * from user_config")
    List<UserConfigDO> listAll();
}
