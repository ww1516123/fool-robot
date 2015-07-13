package com.cqmaple.ai.foolrobot.dao;

import com.cqmaple.ai.foolrobot.model.Words;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ranchaowen on 15-4-21.
 */
@Repository
public interface WordsDao extends PagingAndSortingRepository<Words, Long>,JpaSpecificationExecutor<Words> {
    /**
     * 查询是否存在词语
     * @param words
     * @return
     */
    public Words findByWordsAndEWords(String words,String eWords);
}
