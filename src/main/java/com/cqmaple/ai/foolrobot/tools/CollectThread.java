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
    private Set<String> inserted;
    private Set<String> questions;
    private Set<String> searched;
    private HtmlContents htmlcs;
    private static Random random = null;

    public CollectThread(Set<String> questions,Set<String> searched,Set<String> inserted,HtmlContents htmlcs) {
        this.inserted=inserted;
        this.questions = questions;
        this.htmlcs=htmlcs;
        this.searched=searched;
    }


    @Override
    public void run() {
        while (true) {
            if(!questions.isEmpty()){
                List<String> allChinese=new ArrayList<String>(questions);
               for(String question:allChinese){
                   if(searched.contains(question)){
                       continue;
                   }
                   String[] allWords=null;
                   if(question.indexOf(",")>0){
                       allWords=question.split(",");
                   }else{
                       allWords=new String[1];
                       allWords[0]=question;
                   }
                   String lastHtml=null;
                   for (String worda:allWords){
                       try {
                           if(!"".equals(worda)){
                               System.out.println(new Date().getTime()+"<<<<<<<<<:::"+Thread.currentThread().getName()+"*******************************");
                               System.out.println(Thread.currentThread().getName() + " 开始查询：" + worda);
                               searched.contains(worda);
                               questions.remove(worda);
                               String html ="";
                               Thread.sleep(1000);
                               synchronized (htmlcs){
                                   html=ConnectHelper.BDZD(worda);
                               }
                               htmlcs.getHtmls().add(html);
                               lastHtml=html;
                           }
                       }catch (Exception e){
                           System.out.println(Thread.currentThread().getName()+"////////////////////////////////");
                           System.out.println(Thread.currentThread().getName()+" 开始查询出错一个："+e.getMessage());
                       }
                   }
                   List<QuestionDTO> questionDTOs=null;
                   questionDTOs= ConnectHelper.getPageQA(lastHtml);
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

               }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
