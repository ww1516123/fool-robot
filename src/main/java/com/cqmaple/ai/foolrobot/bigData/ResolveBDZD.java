package com.cqmaple.ai.foolrobot.bigData;

import com.cqmaple.ai.foolrobot.tools.ConnectHelper;
import com.cqmaple.ai.foolrobot.tools.LanguageHelper;
import com.cqmaple.ai.foolrobot.tools.QuestionDTO;
import com.cqmaple.ai.foolrobot.tools.RedisHelper;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.security.provider.MD5;

import java.security.MessageDigest;
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
        String have;
        synchronized (this){
            have=redisHelper.get(MD5(html));
            if(have==null){
                redisHelper.set(MD5(html),"have");
            }
        }
        if(have==null){
            List<QuestionDTO> questionDTOs=new ArrayList<QuestionDTO>();
            try {
                System.out.println(Thread.currentThread().getName() + " 开始一个解析" );
                questionDTOs.addAll(ConnectHelper.getPageQA(html));

            }catch (Exception e){
                e.printStackTrace();
                System.out.println(Thread.currentThread().getName()+"////////////////////////////////");
                System.out.println(Thread.currentThread().getName()+" 解析出错*********************///////////");
                System.out.println(Thread.currentThread().getName()+"////////////////////////////////");
            }

            Set<String> sets=new HashSet<String>();
            for (QuestionDTO q:questionDTOs){
                sets.addAll(getWords(q.getQuestion()));
                sets.addAll(getWords(q.getAnswer()));
            }
            System.out.println(Thread.currentThread().getName() + " 解析*********分析到的词语:"+sets.size() );
            //存储
           for(String word:sets){
               System.out.println(Thread.currentThread().getName() + " 开始存放:"+word );
               redisHelper.Ladd("MS-Resolved-word",word);
               redisHelper.Ladd("MS-Resolved-words",word);
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
            if (redisHelper.Hmget("MS-Resolved2",chinese)!=null) {
                //System.out.println(Thread.currentThread().getName()+"========================&&&&&&保存前发现重复+" + chinese);
                continue;
            }
            redisHelper.Hmset("MS-Resolved2",chinese,"have");
            sets.add(chinese);
        }
        return sets;
    }
    public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            System.out.println("MD5===>"+str);
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
