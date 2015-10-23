package com.cqmaple.ai.foolrobot.bigData;

import com.cqmaple.ai.foolrobot.tools.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by ranchaowen on 15/10/23.
 */
@ContextConfiguration(locations = {"classpath*:spring-context-application.xml"})
public class RunMain {
    @Autowired
    RedisHelper redisHelper;
    @Autowired
    QueryBDZD queryBDZD;
    @Autowired
    ResolveBDZD resolveBDZD;
    @Autowired
    TranslateWord translateWord;
    @Autowired
    SaveWordMain saveWordMain;
    ApplicationContext ctx;

    private void run(){
        ctx = new ClassPathXmlApplicationContext("classpath*:spring-context-application.xml");
        redisHelper= (RedisHelper) ctx.getBean("redisHelper");
        queryBDZD= (QueryBDZD) ctx.getBean("queryBDZD");
        resolveBDZD= (ResolveBDZD) ctx.getBean("resolveBDZD");
        translateWord= (TranslateWord) ctx.getBean("translateWord");
        saveWordMain= (SaveWordMain) ctx.getBean("saveWordMain");

       System.out.print("===>"+ redisHelper.get("name"));
        QueryMain queryMain=new QueryMain(redisHelper,queryBDZD,resolveBDZD,translateWord,saveWordMain);
        Thread thread=new Thread(queryMain);
        thread.start();
    }
    public static void main(String args[]){
        RunMain runMain=new RunMain();
        runMain.run();
    }

}
