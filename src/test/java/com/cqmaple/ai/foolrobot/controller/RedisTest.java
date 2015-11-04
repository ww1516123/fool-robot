//package com.cqmaple.ai.foolrobot.controller;
//
///**
// * Created by Maple on 2015/10/22.
// */
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import com.cqmaple.ai.foolrobot.bigData.*;
//import com.cqmaple.ai.foolrobot.tools.RedisHelper;
//import junit.framework.Assert;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
//
//import javax.annotation.Resource;
//
//
//@ContextConfiguration(locations = {"classpath*:spring-context-application.xml"})
//public class RedisTest extends AbstractJUnit4SpringContextTests {
//        @Autowired
//        RedisHelper redisHelper;
//        @Autowired
//        QueryBDZD queryBDZD;
//        @Autowired
//        ResolveBDZD resolveBDZD;
//        @Autowired
//        TranslateWord translateWord;
//        @Autowired
//        SaveWordMain saveWordMain;
//
//        //@Test
//        public void testSet(){
//            redisHelper.set("n2ame","张三");
//                redisHelper.set("name2","jack");
//        }
//        //@Test
//        public void testGet(){
//
//                redisHelper.get("name2");
//                redisHelper.get("n2ame");
//        }
//        public void testDel(){
//
//                redisHelper.del("name2");
//                redisHelper.del("n2ame");
//        }
//        //@Test
//        public void testHmSet(){
//                redisHelper.Hmset("zd", "name", "maple");
//        }
//        //@Test
//        public void testHmGet(){
//
//                redisHelper.Hmget("zd", "name");
//        }
//        public void testHmDel(){
//                redisHelper.Hmdel("zd", "name");
//        }
//        @Test
//        public void testSetNx(){
//                System.out.println(redisHelper.setNx("bdzd","123"));
//        }
//        @Test
//        public void testQueryBDZD(){
////                for (int i = 0; i <1200 ; i++) {
////                        try {
////                                Thread.sleep(1000L);
////                        } catch (InterruptedException e) {
////                                e.printStackTrace();
////                        }
////                        queryBDZD.queryByKey(getRandomJianHan(2));
////                }
//
////
////                for (String html:redisHelper.Lget("MS-Query-key",-1)){
////                        resolveBDZD.resolveByHtml(html);
////                }
////                for (String word:redisHelper.Lget("MS-Resolved-word",-1)){
////                        translateWord.translate(word);
////                }
////                for (String wordall:redisHelper.Lget("MS-Translated-word",-1)){
////                        saveWordMain.saveOneHundred(wordall);
////                }
////                QueryMain queryMain=new QueryMain(redisHelper,queryBDZD,resolveBDZD,translateWord,saveWordMain);
////                Thread thread=new Thread(queryMain);
////                thread.start();
//
//
//
//
//        }
//        /**
//         * 获取指定长度随机简体中文
//         * @param len int
//         * @return String
//         */
//        public static String getRandomJianHan(int len)
//        {
//                String ret="";
//                for(int i=0;i<len;i++){
//                        String str = null;
//                        int hightPos, lowPos; // 定义高低位
//                        Random random = new Random();
//                        hightPos = (176 + Math.abs(random.nextInt(39))); //获取高位值
//                        lowPos = (161 + Math.abs(random.nextInt(93))); //获取低位值
//                        byte[] b = new byte[2];
//                        b[0] = (new Integer(hightPos).byteValue());
//                        b[1] = (new Integer(lowPos).byteValue());
//                        try
//                        {
//                                str = new String(b, "GBk"); //转成中文
//                        }
//                        catch (UnsupportedEncodingException ex)
//                        {
//                                ex.printStackTrace();
//                        }
//                        ret+=str;
//                }
//                return ret;
//        }
//
//
//        @Test
//        public void mainTest(){
//
////                testSet();
////                testGet();
////                testDel();
//                testHmSet();
//                testHmGet();
//                testHmDel();
//
//        }
//}