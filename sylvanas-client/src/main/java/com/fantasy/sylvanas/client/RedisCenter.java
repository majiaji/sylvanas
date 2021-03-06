package com.fantasy.sylvanas.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

/**
 * Created by jiaji on 2017/6/18.
 */
public class RedisCenter {
    private static final Logger logger = LoggerFactory.getLogger(RedisCenter.class);

    private JedisPool pool;

    void init() {
        JedisPoolConfig config = new JedisPoolConfig();
        //最大资源数(并发数)
        config.setMaxTotal(1024);
        //最大空闲链接数(空闲时保留的活跃链接数)
        config.setMaxIdle(128);
        //没有资源会等待maxWait时间，如果还没有则抛 NoSuchElementException
        config.setBlockWhenExhausted(true);
        config.setMaxWaitMillis(1000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        pool = new JedisPool(config, Protocol.DEFAULT_HOST, Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT);
    }

    public Jedis getInstance() {
        Jedis jedis;
        try {
            jedis = pool.getResource();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
        return jedis;
    }


    void destory() {
        logger.warn("redis poll start destory");
        if (pool != null) {
            pool.destroy();
        }
    }
}
