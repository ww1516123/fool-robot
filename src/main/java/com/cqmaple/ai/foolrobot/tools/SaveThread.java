package com.cqmaple.ai.foolrobot.tools;

import com.cqmaple.ai.foolrobot.common.DuplicateException;
import com.cqmaple.ai.foolrobot.model.Words;
import com.cqmaple.ai.foolrobot.service.WordService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by ranchaowen on 15/7/17.
 */
public class SaveThread implements  Runnable {
    private WordService wordService;
    private Answers answers;

    public SaveThread(WordService wordService, Answers answers) {
        this.wordService = wordService;
        this.answers = answers;
    }

    @Override
    public void run() {
        while (true){
            try {
            System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"*******************************");
            System.out.println(Thread.currentThread().getName() + " 开始查询：一轮保存");
                Thread.sleep(500);

             saveWords();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void saveWords() {

        Set<String> beWords=new HashSet<String>();
        Set<String> canBeSave= answers.getCanBeSave();

        System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"*******************************");
        System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"*******************************");
        System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"*******************************");
        System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"*******************************");
        System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"*******************************");
        System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"*******************************");
        System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"*******************************");
        System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"*******************************");
        System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"*******************************");
        System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"*******************************");
        System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"*******************************");
        System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"*******************************");
        System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"******=============*************************");
        System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"*******================************************");
        System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"*******==============************************");
        System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"***========****************************");
        System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"*******************************");
        System.out.println(Thread.currentThread().getName() + " ===========保存单词有："+canBeSave.size());

        synchronized (answers){
            if(canBeSave.size()>0){
                beWords.addAll(canBeSave);
                canBeSave.clear();
            }
        }
        for (String chinese:beWords){
            String en= null;
            try {
                Thread.sleep(100);
                synchronized (ConnectHelper.class) {
                    en = ConnectHelper.chan2en(chinese);
                }
                Words words=new Words();
                words.setWords(chinese);
                words.seteWords(en);
                wordService.save(words);
            }catch (DuplicateException e){

            } catch(Exception e){
                e.printStackTrace();
            }
        }

    }
}
