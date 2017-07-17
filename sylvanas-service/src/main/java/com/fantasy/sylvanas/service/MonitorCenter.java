package com.fantasy.sylvanas.service;

import com.fantasy.sylvanas.client.RedisCenter;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by jiaji on 2017/7/17.
 */
@Service
public class MonitorCenter {
    private RedisCenter redisCenter;

    Map<String, String> getWordCountList() {
        Jedis jedis = redisCenter.getInstance();
    }
}
