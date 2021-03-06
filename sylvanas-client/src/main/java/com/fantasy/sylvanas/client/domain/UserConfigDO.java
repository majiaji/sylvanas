package com.fantasy.sylvanas.client.domain;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * Created by jiaji on 2017/8/14.
 */
public class UserConfigDO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 服务名
     */
    private String service;

    /**
     * 场景名
     */
    private String scene;
    /**
     * 具体配置
     */
    private String config;

    public static UserConfigDTO convertToDTO(UserConfigDO userConfigDO) {
        if (userConfigDO == null) return null;
        UserConfigDTO userConfigDTO = new UserConfigDTO();
        BeanUtils.copyProperties(userConfigDO, userConfigDTO);
        userConfigDTO.setConfigMap(JSON.parseObject(userConfigDO.getConfig()));
        return userConfigDTO;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

}
