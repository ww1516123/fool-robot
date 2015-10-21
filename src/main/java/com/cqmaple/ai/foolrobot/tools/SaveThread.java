package com.cqmaple.ai.foolrobot.tools;

import com.cqmaple.ai.foolrobot.common.DuplicateException;
import com.cqmaple.ai.foolrobot.model.Words;
import com.cqmaple.ai.foolrobot.service.WordService;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

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
            saveWords();
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void saveWords() {
        Set<String> beWords=new HashSet<String>();
        Set<String> canBeSave= answers.getCanBeSave();
        System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"*******************************");
        System.out.println(Thread.currentThread().getName() + " ===========保存单词有："+canBeSave.size());
        if(canBeSave.size()>0){
            beWords.addAll(canBeSave);
            canBeSave.clear();
        }
        for (String chinese:beWords){
            String en= null;
            try {
                en = ConnectHelper.chan2en(chinese);
                Words words=new Words();
                words.setWords(chinese);
                words.seteWords(en);
                wordService.save(words);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
