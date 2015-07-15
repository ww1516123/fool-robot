package com.cqmaple.ai.foolrobot.tools;

import com.cqmaple.ai.foolrobot.service.WordService;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by ranchaowen on 15/7/14.
 */
public class LoopQuestion implements Runnable {
    private WordService wordService;
    private Set<String> inserted=new HashSet<String>();
    private BlockingQueue queue = new LinkedBlockingQueue();
    private String word;
    private ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(10,50,1,TimeUnit.MINUTES,queue);
    public LoopQuestion(WordService wordService, String word) {
        this.wordService = wordService;
        this.word = word;
    }

    @Override
    public void run() {
        String html = ConnectHelper.BDZD(word);
        List<QuestionDTO> questionDTOs= ConnectHelper.getPageQA(html);
        int i=0;
        for(QuestionDTO questionDTO:questionDTOs){
            String qStr=questionDTO.getAnswer();
            List<Term> terms= ToAnalysis.parse(qStr);
            for (Term term:terms) {
                String chinese = term.getName();
                if(chinese.length()>1){
                    if (LanguageHelper.isChinese(chinese)) {
                        if(i>10){
                            CollectThread collectThread=new CollectThread(this.wordService,chinese,inserted);
                            threadPoolExecutor.execute(collectThread);
                            break;
                        }
                        i++;
                    }
                }
            }
            if(i>5){
                break;
            }
        }
    }
}
