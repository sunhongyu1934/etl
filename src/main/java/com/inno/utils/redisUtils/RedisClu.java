package com.inno.utils.redisUtils;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

public class RedisClu {
    private static JedisCluster cluster;
    static{
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("172.29.237.209", 7000));
        nodes.add(new HostAndPort("172.29.237.209", 7001));
        nodes.add(new HostAndPort("172.29.237.209", 7002));
        nodes.add(new HostAndPort("172.29.237.214", 7003));
        nodes.add(new HostAndPort("172.29.237.214", 7004));
        nodes.add(new HostAndPort("172.29.237.214", 7005));
        nodes.add(new HostAndPort("172.29.237.215", 7006));
        nodes.add(new HostAndPort("172.29.237.215", 7007));
        nodes.add(new HostAndPort("172.29.237.215", 7008));
        cluster=new JedisCluster(nodes);
    }
    public String get(String key){
        String cname=cluster.spop(key);
        return cname;
    }
    public void set(String key,String valye){
        cluster.sadd(key,valye);
    }

    public long getslength(String key){
        return cluster.scard(key);
    }

    public boolean get(String key,String men){
        boolean cname=cluster.sismember(key,men);
        return cname;
    }

    public void remove(String key,String value){
        cluster.srem(key,value);
    }

    public Set<String> getAllset(String key){
        return cluster.smembers(key);
    }

    public void del(String key){
        cluster.del(key);
    }
}
