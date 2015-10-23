package com.cqmaple.ai.foolrobot.bigData;

import com.cqmaple.ai.foolrobot.tools.ConnectHelper;
import com.cqmaple.ai.foolrobot.tools.LanguageHelper;
import com.cqmaple.ai.foolrobot.tools.QuestionDTO;
import com.cqmaple.ai.foolrobot.tools.RedisHelper;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ranchaowen on 15/10/23.
 */
@Component
public class ResolveBDZD {
    @Autowired
    RedisHelper redisHelper;

    public void resolveByHtml(String html){
//        List<String> reuslts=redisHelper.Lget("MS-Query-key",100);
//        for (int i = 0; i <reuslts.size() ; i++) {
//            redisHelper.LDel(reuslts.get(i));
//        }
        if(redisHelper.get(html)==null){
            List<QuestionDTO> questionDTOs=new ArrayList<QuestionDTO>();
            try {
                System.out.println(Thread.currentThread().getName() + " 开始一个解析" );
                questionDTOs.addAll(ConnectHelper.getPageQA(html));

            }catch (Exception e){
                System.out.println(Thread.currentThread().getName()+"////////////////////////////////");
                System.out.println(Thread.currentThread().getName()+" 解析出错*********************///////////");
                System.out.println(Thread.currentThread().getName()+"////////////////////////////////");
            }
            redisHelper.set(html,questionDTOs.toString());
            Set<String> sets=new HashSet<String>();
            for (QuestionDTO q:questionDTOs){
                sets.addAll(getWords(q.getQuestion()));
                sets.addAll(getWords(q.getAnswer()));
            }
            //存储
           for(String word:sets){
               redisHelper.Ladd("MS-Resolved-word",word);
           }
        }



    }

    private Set<String> getWords(String str){
        Set<String> sets=new HashSet<String>();
        List<Term> terms= ToAnalysis.parse(str);
        //对提问进行分词
        for (Term term:terms) {
            String chinese = term.getName();
            if(!LanguageHelper.isChinese(chinese)){
                continue;
            }
            //这里需要判断重复
            if (redisHelper.Hmget("MS-Resolved",chinese)!=null) {
                //System.out.println(Thread.currentThread().getName()+"========================&&&&&&保存前发现重复+" + chinese);
                continue;
            }
            redisHelper.Hmset("MS-Resolved",chinese,"have");
            sets.add(chinese);
        }
        return sets;
    }
}
