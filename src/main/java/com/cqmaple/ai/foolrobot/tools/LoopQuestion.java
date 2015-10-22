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
    private Set<String> searchChinese=new HashSet<String>();
    private Set<String> searched=new HashSet<String>();
    private BlockingQueue queue = new LinkedBlockingQueue();
    private HtmlContents htmlcs=new HtmlContents();
    private Answers answers=new Answers();
    private String word;

    private ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(1,1,1,TimeUnit.MINUTES,queue);
    private ThreadPoolExecutor resolveThreads=new ThreadPoolExecutor(1,1,1,TimeUnit.MINUTES,queue);
    private ThreadPoolExecutor saveThreads=new ThreadPoolExecutor(2,2,1,TimeUnit.MINUTES,queue);
    public LoopQuestion(WordService wordService, String word) {
        this.wordService = wordService;
        this.word = word;
    }

    @Override
    public void run() {
        //查询答案
        String html = ConnectHelper.BDZD(word);
        inserted.add(word);
        //解析答案
        List<QuestionDTO> questionDTOs= ConnectHelper.getPageQA(html);
        int i=0;
        CollectThread collectThread=null;
        for(QuestionDTO questionDTO:questionDTOs){
            //获取单个回答
            String qStr=questionDTO.getAnswer();
            //回答分词
            List<Term> terms= ToAnalysis.parse(qStr);
            for (Term term:terms) {
                //分别入库
                String chinese = term.getName();
                if(chinese.length()>1){
                    //判断是否中文
                    if (LanguageHelper.isChinese(chinese)) {
                        //开启线程
                        searchChinese.add(chinese);
                    }
                    collectThread =new CollectThread(searchChinese,searched,inserted,htmlcs);


                }
            }
            if(i>10){
                break;
            }
        }
        threadPoolExecutor.execute(collectThread);
        answers.setSearchWord(searchChinese);

        ResolveThread resolveThread=new ResolveThread(answers,htmlcs);
        resolveThreads.execute(resolveThread);
        //添加保存线程
        for (int j = 0; j <2 ; j++) {
            SaveThread saveThread=new SaveThread(wordService,answers);
            saveThreads.execute(saveThread);
        }
    }
}
