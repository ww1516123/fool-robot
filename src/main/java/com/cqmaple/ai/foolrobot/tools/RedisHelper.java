package com.cqmaple.ai.foolrobot.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Maple on 2015/10/22.
 */
@Component
public class RedisHelper {
    @Autowired
    private StringRedisTemplate redisTemplate;
    public static void main(String args[]){
        Jedis jedis=new Jedis("222.177.14.57",3769);
        for (int i = 0; i <72000 ; i++) {
            try {
                Thread.sleep(1000L);
                String key=getRandomJianHan(2);
                System.out.println(Thread.currentThread().getName() + " 开始查询：" + key);
                String html= ConnectHelper.BDZD(key);
                jedis.lpush("MS-Query-key", html);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
//
//        System.out.println(jedis.mget("MS-Resolved","重庆"));
//        System.out.println(jedis.llen("MS-Resolved-words"));

    }
    /**
     * 获取指定长度随机简体中文
     * @param len int
     * @return String
     */
    private static String getRandomJianHan(int len)
    {
        String ret="";
        for(int i=0;i<len;i++){
            String str = null;
            int hightPos, lowPos; // 定义高低位
            Random random = new Random();
            hightPos = (176 + Math.abs(random.nextInt(39))); //获取高位值
            lowPos = (161 + Math.abs(random.nextInt(93))); //获取低位值
            byte[] b = new byte[2];
            b[0] = (new Integer(hightPos).byteValue());
            b[1] = (new Integer(lowPos).byteValue());
            try
            {
                str = new String(b, "GBk"); //转成中文
            }
            catch (UnsupportedEncodingException ex)
            {
                ex.printStackTrace();
            }
            ret+=str;
        }
        return ret;
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
