package com.cqmaple.ai.foolrobot.tools;

import com.cqmaple.ai.foolrobot.common.DuplicateException;
import com.cqmaple.ai.foolrobot.model.Words;
import com.cqmaple.ai.foolrobot.service.WordService;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
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
        List<Term> terms= ToAnalysis.parse(str);
        //对提问进行分词
        for (Term term:terms){
            String chinese=term.getName();
            if(isChinese(chinese)){
                String en= null;
                try {
                    en = ConnectHelper.chan2en(chinese);
                    Words words=new Words();
                    words.setWords(chinese);
                    words.seteWords(en);
                    wordService.save(words);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }catch (DuplicateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 验证所有字都是中文
     * @param str
     * @return
     */
    private boolean isChinese(String str){
        if(str==null||str.equals("")){
            return false;
        }
        boolean isGB2312=false;
        int count = 0;
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                count = count + 1;
            }
        }
        return count==str.length();
    }

}
