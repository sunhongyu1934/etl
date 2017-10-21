package com.inno.utils.redisUtils;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

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

    public void remove(String key,String value){
        jedis.srem(key,value);
    }

    public Set<String> getOnid(String key){
        return jedis.hkeys(key);
    }

    public String getName(String key){
        return jedis.hget("t_id_name_all",key);
    }


}