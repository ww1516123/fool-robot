package com.cqmaple.ai.foolrobot.bigData;

import com.cqmaple.ai.foolrobot.tools.ConnectHelper;
import com.cqmaple.ai.foolrobot.tools.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by ranchaowen on 15/10/23.
 */
@Component
public class TranslateWord {
    @Autowired
    RedisHelper redisHelper;

    public  void translate(String chinese){
        String have;
        synchronized (this){
             have=redisHelper.Hmget("MS-Translated", chinese);
             if(have==null) {
                 redisHelper.Hmset("MS-Translated",chinese,"have");
             }
        }
        if(have==null){
            try{
                String  en = ConnectHelper.chan2en(chinese);
                System.out.println(Thread.currentThread().getName() + " 翻译完成：" + chinese + " 英文:"+en);

                redisHelper.Ladd("MS-Translated-word",chinese+";"+en);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }
}
