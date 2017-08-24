package com.fantasy.sylvanas.service.dao;

import com.fantasy.sylvanas.client.domain.UserConfigDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jiaji on 2017/8/14.
 */
@Repository
public interface IUserConfigDAO {
    @Select("select * from user_config")
    List<UserConfigDO> listAll();

    @Select("select * from user_config where service=#{service} and scene = #{scene}")
    UserConfigDO getByServiceAndScene(@Param("service") String service, @Param("scene") String scene);

    @Update("update user_config set config = #{config} where id = #{id}")
    int uodateConfig(@Param("id") Long id, @Param("config") String config);
}
