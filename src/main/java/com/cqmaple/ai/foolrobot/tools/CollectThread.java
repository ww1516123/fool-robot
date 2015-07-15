package com.cqmaple.ai.foolrobot.tools;

import com.cqmaple.ai.foolrobot.common.DuplicateException;
import com.cqmaple.ai.foolrobot.model.Words;
import com.cqmaple.ai.foolrobot.service.WordService;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ranchaowen on 15/7/14.
 */
public class CollectThread implements  Runnable {
    private WordService wordService;
    private List<QuestionDTO> questionDTOs;
    private Set<String> inserted;
    private String question;

    public CollectThread(WordService wordService,String question,Set<String> inserted) {
        this.wordService = wordService;
        this.inserted=inserted;
        this.question = question;
    }

    @Override
    public void run() {
        while (true) {
            String[] allWords=null;
            if(question.indexOf(",")>0){
                allWords=question.split(",");
            }else{
                allWords=new String[1];
                allWords[0]=question;
            }
            List<QuestionDTO> questionDTOs=null;
            for (String worda:allWords){
                if(!"".equals(worda)){
                    String html = ConnectHelper.BDZD(worda);
                    questionDTOs= ConnectHelper.getPageQA(html);
                    //saveWords(question);
                    for (QuestionDTO questionDTO:questionDTOs){
                        saveWords(questionDTO.getAnswer());
                        saveWords(questionDTO.getQuestion());
                    }
                }
            }
            if(questionDTOs!=null&&questionDTOs.size()>0){
                String qStr=questionDTOs.get(0).getAnswer();
                List<Term> terms= ToAnalysis.parse(qStr);
                int i=0;
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
    private void saveWords(String str) {
        List<Term> terms= ToAnalysis.parse(str);
        //对提问进行分词
        for (Term term:terms){
            String chinese=term.getName();
            if(LanguageHelper.isChinese(chinese)){
                String en= null;
                try {
                    en = ConnectHelper.chan2en(chinese);
                    Words words=new Words();
                    words.setWords(chinese);
                    words.seteWords(en);
                    wordService.save(words);
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
}
