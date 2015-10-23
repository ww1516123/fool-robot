package com.cqmaple.ai.foolrobot.bigData;

import com.cqmaple.ai.foolrobot.common.BaseVo;
import com.cqmaple.ai.foolrobot.common.ResponseMsg;
import com.cqmaple.ai.foolrobot.controller.WordController;
import com.cqmaple.ai.foolrobot.controller.vo.WordTypeVo;
import com.cqmaple.ai.foolrobot.tools.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by ranchaowen on 15/10/23.
 */
public class QueryMain implements  Runnable{
    private BlockingQueue queue = new LinkedBlockingQueue();
    private ThreadPoolExecutor queryPoolExecutor=new ThreadPoolExecutor(2,2,1, TimeUnit.MINUTES,queue);
    private ThreadPoolExecutor resolveThreads=new ThreadPoolExecutor(3,3,1,TimeUnit.MINUTES,queue);
    private ThreadPoolExecutor translateThreads=new ThreadPoolExecutor(3,3,1,TimeUnit.MINUTES,queue);
    private ThreadPoolExecutor saveThreads=new ThreadPoolExecutor(1,1,1,TimeUnit.MINUTES,queue);

    RedisHelper redisHelper;
    QueryBDZD queryBDZD;
    ResolveBDZD resolveBDZD;
    TranslateWord translateWord;

    SaveWordMain saveWordMain;

    public QueryMain(RedisHelper redisHelper, QueryBDZD queryBDZD, ResolveBDZD resolveBDZD, TranslateWord translateWord, SaveWordMain saveWordMain) {
        this.redisHelper = redisHelper;
        this.queryBDZD = queryBDZD;
        this.resolveBDZD = resolveBDZD;
        this.translateWord = translateWord;
        this.saveWordMain = saveWordMain;
    }

    public RedisHelper getRedisHelper() {
        return redisHelper;
    }

    public void setRedisHelper(RedisHelper redisHelper) {
        this.redisHelper = redisHelper;
    }

    public QueryBDZD getQueryBDZD() {
        return queryBDZD;
    }

    public void setQueryBDZD(QueryBDZD queryBDZD) {
        this.queryBDZD = queryBDZD;
    }

    public ResolveBDZD getResolveBDZD() {
        return resolveBDZD;
    }

    public void setResolveBDZD(ResolveBDZD resolveBDZD) {
        this.resolveBDZD = resolveBDZD;
    }

    public TranslateWord getTranslateWord() {
        return translateWord;
    }

    public void setTranslateWord(TranslateWord translateWord) {
        this.translateWord = translateWord;
    }

    public SaveWordMain getSaveWordMain() {
        return saveWordMain;
    }

    public void setSaveWordMain(SaveWordMain saveWordMain) {
        this.saveWordMain = saveWordMain;
    }

    @Override
    public void run() {
        for (int i = 0; i <2 ; i++) {
            Runnable queryThread= new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        List<String> words;
                        synchronized (WordController.class) {
                            String loadCount = "0";
                            if (redisHelper.get("MS-load-searh-count") == null) {
                                redisHelper.set("MS-load-searh-count", "0");
                            } else {
                                loadCount = redisHelper.get("MS-load-searh-count");
                            }
                            Long count = Long.valueOf(loadCount);


                            words = redisHelper.Lget("MS-Resolved-word", (count + 10));
                            redisHelper.set("MS-load-searh-count", Long.valueOf((count + words.size())).toString());
                        }
                        for (int i = 0; i < words.size(); i++) {
                            try {
                                Thread.sleep(1000L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            queryBDZD.queryByKey(words.get(i));
                        }
                    }
                }
            };
            queryPoolExecutor.execute(queryThread);
        }
        for (int i = 0; i <3 ; i++) {
            Runnable resolveThread= new Runnable() {
                @Override
                public void run() {
                   while (true){
                       List<String> htmls;
                       synchronized (BaseVo.class) {
                           htmls = redisHelper.Lget("MS-Query-key", 100);
                           for (int j = 0; j < htmls.size(); j++) {
                               redisHelper.LDel("MS-Query-key");
                           }
                       }
                       for (String html : htmls) {

                           resolveBDZD.resolveByHtml(html);
                       }
                   }
                }
            };
            resolveThreads.execute(resolveThread);
        }
        for (int i = 0; i <3 ; i++) {
            Runnable translateThread= new Runnable() {
                @Override
                public void run() {
                    while (true){
                        List<String> words;
                        synchronized (ResponseMsg.class) {
                            words = redisHelper.Lget("MS-Resolved-word", 100);

                            for (int j = 0; j < words.size(); j++) {
                                redisHelper.LDel("MS-Resolved-word");
                            }
                        }
                        for (String word : words) {
                            try {
                                Thread.sleep(1000L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            translateWord.translate(word);
                        }
                    }
                }
            };
            translateThreads.execute(translateThread);
        }
        for (int i = 0; i <1 ; i++) {
            Runnable saveThread= new Runnable() {
                @Override
                public void run() {
                    while (true){
                        List<String> words;
                        synchronized (WordTypeVo.class) {
                            //System.out.println(Thread.currentThread().getName() + " 开始保存词语:开始抓");
                            words = redisHelper.Lget("MS-Translated-word", 100);
                            for (int j = 0; j < words.size(); j++) {
                                redisHelper.LDel("MS-Translated-word");
                            }
                           // System.out.println(Thread.currentThread().getName() + " 完成:抓");
                        }
                        for (String wordall : words) {
                            saveWordMain.saveOneHundred(wordall);
                        }
                    }


                }
            };
            saveThreads.execute(saveThread);
        }

    }
}
