package com.cqmaple.ai.foolrobot.service;

import com.cqmaple.ai.foolrobot.common.DuplicateException;
import com.cqmaple.ai.foolrobot.dao.WordsDao;
import com.cqmaple.ai.foolrobot.model.Words;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by ranchaowen on 15-4-21.
 */
@Service
public class WordService {

    @Resource
    private WordsDao wordsDao;

    @Transactional(readOnly = false)
    public void save(Words words) throws DuplicateException {

        if(wordsDao.findByWordsAndEWords(words.getWords(),words.geteWords())!=null){
            throw new DuplicateException(Words.class,"words");
        }
        wordsDao.save(words);
    }
    /**
     * 获取所有词语
     * @param pageable
     * @return
     */
    public Page<Words> findAll(Pageable pageable){

       return  this.wordsDao.findAll(pageable);
    }
}
