package com.cqmaple.ai.foolrobot.bigData;

import com.cqmaple.ai.foolrobot.common.DuplicateException;
import com.cqmaple.ai.foolrobot.model.Words;
import com.cqmaple.ai.foolrobot.service.WordService;
import com.cqmaple.ai.foolrobot.tools.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ranchaowen on 15/10/23.
 */
@Component
public class SaveWordMain {
    @Autowired
    RedisHelper redisHelper;
    @Autowired
    WordService wordService;

    public  void  saveOneHundred(String word){
        try {
            String[] wordall= word.split(";");
            String have;
            synchronized (this){
                have=redisHelper.Hmget("MS-word-saved", wordall[0]);
                if(have==null){
                    redisHelper.Hmset("MS-word-saved", wordall[0], "have");
                }
            }
            if(have==null){
                Words temp=new Words();
                temp.setWords(wordall[0]);
                temp.seteWords(wordall[1]);
                System.out.println(Thread.currentThread().getName() + " 开始保存词语:"+wordall[0]);
                wordService.save(temp);
            }
        } catch (DuplicateException e1){

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
