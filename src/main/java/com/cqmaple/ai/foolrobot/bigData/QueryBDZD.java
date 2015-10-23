package com.cqmaple.ai.foolrobot.bigData;

import com.cqmaple.ai.foolrobot.tools.ConnectHelper;
import com.cqmaple.ai.foolrobot.tools.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ranchaowen on 15/10/23.
 */
@Component
public class QueryBDZD {
    @Autowired
    RedisHelper redisHelper;
    //查询并保存
    public void queryByKey(String key){
        String have=null;

        synchronized (this){
            have=redisHelper.get(key);
            if(have==null){
                redisHelper.set(key, "have");
            }
        }
        if(null==have){
            System.out.println(Thread.currentThread().getName() + " 开始查询：" + key);
            String html= ConnectHelper.BDZD(key);
            redisHelper.Ladd( "MS-Query-key",html);

        }
    }
}
