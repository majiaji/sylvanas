package com.fantasy.sylvanas.service;

import com.fantasy.sylvanas.client.RedisCenter;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by jiaji on 2017/7/17.
 */
@Service
public class MonitorCenter {

    @Resource
    private RedisCenter redisCenter;

    public Map<String, String> getWordCount(String key) {
        Jedis jedis = redisCenter.getInstance();
        Map<String, String> result = jedis.hgetAll(key);
        jedis.close();
        return result;
    }
}
