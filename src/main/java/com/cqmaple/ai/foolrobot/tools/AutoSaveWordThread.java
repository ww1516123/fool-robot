package com.cqmaple.ai.foolrobot.tools;

import com.cqmaple.ai.foolrobot.common.DuplicateException;
import com.cqmaple.ai.foolrobot.model.Words;
import com.cqmaple.ai.foolrobot.service.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ranchaowen on 15/7/13.
 */
public class AutoSaveWordThread implements  Runnable {
    private Logger logger= LoggerFactory.getLogger(AutoSaveWordThread.class);
    private WordService wordService;
    private List<QuestionDTO> questionDTOs;
    private String question;

    public AutoSaveWordThread(WordService wordService, List<QuestionDTO> questionDTOs, String question) {
        this.wordService = wordService;
        this.questionDTOs = questionDTOs;
        this.question = question;
    }
    @Override
    public void run() {

            saveWords(question);
            for (QuestionDTO questionDTO:questionDTOs){
                saveWords(questionDTO.getAnswer());
                saveWords(questionDTO.getQuestion());
            }


    }
    private void saveWords(String str) {
//        List<Term> terms= ToAnalysis.parse(str);
//        //对提问进行分词
//        for (Term term:terms){
//            String chinese=term.getName();
//            if(LanguageHelper.isChinese(chinese)){
//                String en= null;
//                try {
//                    en = ConnectHelper.chan2en(chinese);
//                    Words words=new Words();
//                    words.setWords(chinese);
//                    words.seteWords(en);
//                    wordService.save(words);
//                } catch (IOException e) {
//                   // e.printStackTrace();
//                } catch (URISyntaxException e) {
//                    //e.printStackTrace();
//                }catch (DuplicateException e) {
//                    //e.printStackTrace();
//                }catch (NullPointerException e){
//
//                }
//            }
//        }
    }



}
