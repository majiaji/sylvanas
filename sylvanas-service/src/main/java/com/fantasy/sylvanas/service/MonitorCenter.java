package com.fantasy.sylvanas.service;

import com.fantasy.sylvanas.client.RedisCenter;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

/**
 * Created by jiaji on 2017/7/17.
 */
@Service
public class MonitorCenter {

    @Resource
    private RedisCenter redisCenter;

    public String getWordCount(String key) {
        Jedis jedis = redisCenter.getInstance();
        String result = jedis.get(key);
        jedis.close();
        return result;
    }
}
