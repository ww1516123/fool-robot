package com.cqmaple.ai.foolrobot.tools;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.*;

/**
 * Created by ranchaowen on 15/7/17.
 */
public class ResolveThread implements  Runnable {
    private  Answers answers;
    private HtmlContents htmlContents;

    public ResolveThread(Answers answers, HtmlContents htmlContents) {
        this.answers = answers;
        this.htmlContents = htmlContents;
    }

    @Override
    public void run() {
        while (true){

            List<QuestionDTO> questionDTOs=new ArrayList<QuestionDTO>();
            for (String html:htmlContents.getByCount(10)){
                questionDTOs.addAll(ConnectHelper.getPageQA(html));
            }
            for (QuestionDTO questionDTO:questionDTOs){
                synchronized (answers){
                    answers.getCanBeSave().addAll(resolveAnswer(questionDTO));
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    private  Set<String> resolveAnswer(QuestionDTO questionDTO){
        Set<String> sets=new HashSet<String>();
        sets.addAll(getWords(questionDTO.getQuestion()));
        sets.addAll(getWords(questionDTO.getAnswer()));
        return sets;
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
            if (answers.getSaved().contains(chinese)) {
                //System.out.println(Thread.currentThread().getName()+"========================&&&&&&保存前发现重复+" + chinese);
                continue;
            }
            synchronized (answers){
                answers.getSaved().add(chinese);
            }
            sets.add(chinese);
        }
        return sets;
    }
}
