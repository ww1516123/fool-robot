package com.cqmaple.ai.foolrobot.dao;

import com.cqmaple.ai.foolrobot.model.Words;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ranchaowen on 15-4-21.
 */
@Repository
public interface WordsDao extends PagingAndSortingRepository<Words, Long> {

}
