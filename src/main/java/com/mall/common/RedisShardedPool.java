package com.mall.common;

import com.mall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;

import java.util.ArrayList;
import java.util.List;

public class RedisShardedPool {
    private static ShardedJedisPool pool;//shardedJedis连接池
    //最大连接数
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));
    //JedisPool中最大的idle状态（空闲）的jedis实例的个数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));
    //JedisPool中最小的idle状态（空闲）的jedis实例的个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "2"));
    //在borrow一个jedis实例的时候，是否要验证操作，如果赋值为true，则得到的jedis实例肯定是可以用的。
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));
    //在return一个jedis实例的时候，是否要验证操作，如果赋值为true，则放回jedisPool的jedis实例肯定是可以用的。
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));

    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));
    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");
    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        //连接耗尽是是否阻塞，默认为true，false会抛出异常，true会阻塞知道超时
        config.setBlockWhenExhausted(true);
        JedisShardInfo info1 = new JedisShardInfo(redis1Ip, redis1Port, 1000 * 2);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip, redis2Port, 1000 * 2);
        List<JedisShardInfo> jedisShardInfoList = new ArrayList<>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);

        pool = new ShardedJedisPool(config, jedisShardInfoList, Hashing.MURMUR_HASH, ShardedJedis.DEFAULT_KEY_TAG_PATTERN);

    }

    static {
        initPool();
    }

    public static ShardedJedis getShardedJedis() {
        return pool.getResource();
    }

    public static void returnResource(ShardedJedis shardedJedis) {
        pool.returnResource(shardedJedis);
    }

    public static void returnBrokenResource(ShardedJedis shardedJedis) {
        pool.returnBrokenResource(shardedJedis);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();
        for (int i = 0; i < 10; i++) {
            jedis.set("key" + i, "value" + i);
        }
        returnResource(jedis);
//        pool.destroy();//临时调用，销毁连接池中的所有连接
        System.out.println("program is end");
    }
}
