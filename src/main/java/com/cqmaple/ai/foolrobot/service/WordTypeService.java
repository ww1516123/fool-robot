package com.cqmaple.ai.foolrobot.service;

import com.cqmaple.ai.foolrobot.common.DuplicateException;
import com.cqmaple.ai.foolrobot.dao.WordTypeDao;
import com.cqmaple.ai.foolrobot.model.WordType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Created by ranchaowen on 15-4-24.
 */
@Service
public class WordTypeService {
    @Resource
    private WordTypeDao wordTypeDao;

    @Transactional
    public void save(WordType wordType) throws DuplicateException{
        if(wordTypeDao.findByName(wordType.getName())!=null){
            throw new DuplicateException(WordType.class,"name");
        }
        wordTypeDao.save(wordType);

    }

    public WordType findByName(String name){
        return this.wordTypeDao.findByName(name);
    }

    public List<WordType> findAll() {
        return wordTypeDao.findAll();
    }

}
