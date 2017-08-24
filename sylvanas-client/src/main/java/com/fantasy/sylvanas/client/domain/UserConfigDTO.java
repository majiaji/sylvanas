package com.fantasy.sylvanas.client.domain;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.Map;

/**
 * Created by jiaji on 2017/8/20.
 */
public class UserConfigDTO {
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
    private Map<String, Object> configMap;

    public static UserConfigDO convertToDO(UserConfigDTO userConfigDTO) {
        if (userConfigDTO == null) return null;
        UserConfigDO userConfigDO = new UserConfigDO();
        BeanUtils.copyProperties(userConfigDTO, userConfigDO);
        userConfigDO.setConfig(JSON.toJSONString(userConfigDTO.getConfigMap()));
        return userConfigDO;
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

    public Map<String, Object> getConfigMap() {
        return configMap;
    }

    public void setConfigMap(Map<String, Object> configMap) {
        this.configMap = configMap;
    }
}
