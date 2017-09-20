package com.inno.utils.redisUtils;

import redis.clients.jedis.Jedis;

public class RedisAction {
    private Jedis jedis;
    public RedisAction(String host, int port){
        jedis=new Jedis(host,port);
    }
    public boolean get(String key,String men){
        boolean cname=jedis.sismember(key,men);
        return cname;
    }
    public void set(String key,String valye){
        jedis.sadd(key,valye);
    }
}