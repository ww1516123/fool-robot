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
    private Set<String> saved=new HashSet<String>();
    private BlockingQueue queue = new LinkedBlockingQueue();
    private HtmlContents htmlcs=new HtmlContents();
    private Answers answers=new Answers();
    private String word;

    private ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(10,50,1,TimeUnit.MINUTES,queue);
    private ThreadPoolExecutor resolveThreads=new ThreadPoolExecutor(5,20,1,TimeUnit.MINUTES,queue);
    private ThreadPoolExecutor saveThreads=new ThreadPoolExecutor(1,2,1,TimeUnit.MINUTES,queue);
    public LoopQuestion(WordService wordService, String word) {
        this.wordService = wordService;
        this.word = word;
    }

    @Override
    public void run() {
        String html = ConnectHelper.BDZD(word);
        inserted.add(word);
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
                            break;
                        }
                        CollectThread collectThread=new CollectThread(chinese,inserted,htmlcs);
                        threadPoolExecutor.execute(collectThread);
                        i++;
                    }
                }
            }
            if(i>10){
                break;
            }
        }
        //添加解析线程
        for (int j = 0; j < 10; j++) {
            ResolveThread resolveThread=new ResolveThread(answers,htmlcs);
            resolveThreads.execute(resolveThread);
        }
        //添加解析线程
       SaveThread saveThread=new SaveThread(wordService,answers);
        saveThreads.execute(saveThread);

    }
}
