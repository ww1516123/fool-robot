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
            saveWords();
        }
    }
    private void saveWords() {
        Set<String> beWords=new HashSet<String>();
        Set<String> canBeSave= answers.getCanBeSave();
        if(canBeSave.size()>0){
            beWords.addAll(canBeSave);
        }
        for (String chinese:beWords){
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
