package com.cqmaple.ai.foolrobot.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

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
        System.out.println(jedis.hget("insertd","age"));

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
        Long result=redisTemplate.opsForSet().add(key,value);
        System.out.println(result+"<<<<<<<<====================");
        return true;
    }

}
