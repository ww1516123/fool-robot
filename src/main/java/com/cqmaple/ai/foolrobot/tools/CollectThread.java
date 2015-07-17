package com.cqmaple.ai.foolrobot.tools;

import com.cqmaple.ai.foolrobot.common.DuplicateException;
import com.cqmaple.ai.foolrobot.model.Words;
import com.cqmaple.ai.foolrobot.service.WordService;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by ranchaowen on 15/7/14.
 */
public class CollectThread implements  Runnable {
    private WordService wordService;
    private Set<String> inserted;
    private Set<String> saved;
    private String question;
    private static Random random = null;

    public CollectThread(WordService wordService,String question,Set<String> inserted,Set<String> saved) {
        this.wordService = wordService;
        this.inserted=inserted;
        this.saved=saved;
        this.question = question;
    }

    @Override
    public void run() {
        while (question!=null&&(!question.equals(""))) {
            String[] allWords=null;
            if(question.indexOf(",")>0){
                allWords=question.split(",");
            }else{
                allWords=new String[1];
                allWords[0]=question;
            }
            List<QuestionDTO> questionDTOs=null;
            for (String worda:allWords){
                try {
                    if(!"".equals(worda)){
                        System.out.println(Thread.currentThread().getName()+"*******************************");
                        System.out.println(Thread.currentThread().getName()+" 开始查询："+worda);
                        String html ="";
                        Thread.sleep(1000);
                        synchronized (wordService){
                            html=ConnectHelper.BDZD(worda);
                        }
                        //System.out.println(Thread.currentThread().getName()+"*******************************");
                        //System.out.println(Thread.currentThread().getName()+" 查询完毕："+html);
                        //System.out.println(Thread.currentThread().getName()+" 查询完毕：代码完代码完代码完代码完====");
                        questionDTOs= ConnectHelper.getPageQA(html);
                        //saveWords(question);
                        for (QuestionDTO questionDTO:questionDTOs){
                            //System.out.println(Thread.currentThread().getName()+"##############开始保存"+questionDTO.toString());
                            saveWords(questionDTO.getAnswer());
                            saveWords(questionDTO.getQuestion());
                        }
                    }
                }catch (Exception e){
                    System.out.println(Thread.currentThread().getName()+"////////////////////////////////");
                    System.out.println(Thread.currentThread().getName()+" 开始查询出错一个："+e.getMessage());
                }

            }
            if(questionDTOs!=null&&questionDTOs.size()>0){
                int i=0;
                for (QuestionDTO questionDTO:questionDTOs){
                    String qStr=questionDTO.getAnswer();
                    List<Term> terms= ToAnalysis.parse(qStr);
                    for (Term term:terms) {
                        String chinese = term.getName();
                        if(chinese.length()>1){
                            if (LanguageHelper.isChinese(chinese)) {
                                if(inserted.contains(chinese)){
                                    continue;
                                }
                                if(i==0){
                                    question="";
                                }else{
                                    question+=",";
                                }
                                question+=chinese;
                                inserted.add(chinese);
                                i++;
                            }
                        }
                    }
                }

            }
        }
        System.out.println("=========<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println("=========<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println(Thread.currentThread().getName()+"挂啦~~~~");
        System.out.println("=========<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println("=========<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }
    private void saveWords(String str) {
        List<Term> terms= ToAnalysis.parse(str);
        //对提问进行分词
        for (Term term:terms){
            String chinese=term.getName();
            System.out.println("解析到=======================>词语::::"+chinese);
            if(saved.contains(chinese)){
                System.out.println("&&&&&&&&&&&&&&&&&&&&&&保存前发现重复+"+chinese);
                continue;
            }
            if(LanguageHelper.isChinese(chinese)){
                String en= null;
                try {
                    en = ConnectHelper.chan2en(chinese);
                    Words words=new Words();
                    words.setWords(chinese);
                    words.seteWords(en);
                    wordService.save(words);
                    saved.add(chinese);
                } catch (IOException e) {
                    // e.printStackTrace();
                } catch (URISyntaxException e) {
                    //e.printStackTrace();
                }catch (DuplicateException e) {
                    //e.printStackTrace();
                }catch (NullPointerException e){

                }catch (Exception e){

                }
            }
        }
    }
    private static Random getRandomInstance() {
        if (random == null) {
            random = new Random(new Date().getTime());
        }
        return random;
    }

    public static String getChinese() {
        String str = null;
        int highPos, lowPos;
        Random random = getRandomInstance();
        highPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = 161 + Math.abs(random.nextInt(93));
        byte[] b = new byte[2];
        b[0] = (new Integer(highPos)).byteValue();
        b[1] = (new Integer(lowPos)).byteValue();
        try {
            str = new String(b, "GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }
    public static String getFixedLengthChinese(int length) {
        String str = "";
        for (int i = length; i > 0; i--) {
            str = str + CollectThread.getChinese();
        }
        return str;
    }
    public static void main(String args[]){
//        Set<String> sets=new HashSet<>();
//        Long time=new Date().getTime();
//        for (int i = 0; i < 1000000; i++) {
//            int intValue=(int)(Math.random()*6+2);
//            sets.add(getFixedLengthChinese(intValue));
//        }
//
//        System.out.println("添加100W个中文耗时:" + (new Date().getTime()-time)+" 毫秒");
//        List<String> val=new ArrayList<>();
//        for (String str:sets) {
//            if(str.length()>2&&str.length()<6){
//                int intValue=(int)(Math.random()*6+2);
//                if(intValue==4){
//                    val.add(str);
//                }
//            }
//
//        }
//
//        for (String str:val) {
//            time=new Date().getTime();
//            System.out.println(str);
//            sets.contains(str);
//            System.out.println("查询:" + (new Date().getTime() - time) + " 毫秒");
//        }
    }
}
