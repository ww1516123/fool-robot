package com.cqmaple.ai.foolrobot.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Maple on 2015/10/22.
 */
@Component
public class RedisHelper {
    @Autowired
    private StringRedisTemplate redisTemplate;
    public static void main(String args[]){
        Jedis jedis=new Jedis("127.0.0.1");
        jedis.hset("insertd","age","123");
        System.out.println(jedis.hget("insertd", "age"));

    }
    /**
      //execute a transaction
     List<Object> txResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
     public List<Object> execute(RedisOperations operations) throws DataAccessException {
     operations.multi();
     operations.opsForSet().add("key", "value1");

     // This will contain the results of all ops in the transaction
     return operations.exec();
     }
     });
     System.out.println("Number of items added to set: " + txResults.get(0));

     */
    public boolean set(String key,String value){
        redisTemplate.opsForValue().set(key, value);
        return true;
    }
    public String get(String key){
        String result=redisTemplate.opsForValue().get(key);
        return result;
    }
    public void Hmset(String key,String filed,String value){
        redisTemplate.opsForHash().put(key, filed, value);
    }
    public String Hmget(String key,String filed){
       return (String) redisTemplate.opsForHash().get(key,filed);
    }
    public void Hmdel(String key,String filed){
         redisTemplate.opsForHash().delete(key, filed);
    }
    public void Ladd(String key,String value){
        redisTemplate.opsForList().leftPush(key,value);
    }
    public List<String> Lget(String key,long end){
        if(end==0){
            end=-1;
        }
        List<String> meanings = redisTemplate.opsForList().range(key, 0, end);
        return meanings;
    }

    public String LDel(String key){
        return redisTemplate.opsForList().rightPop(key);
    }

    public boolean del(String key){
        redisTemplate.delete(key);
        return true;
    }


}
