package com.cqmaple.ai.foolrobot.dao;

import com.cqmaple.ai.foolrobot.model.WordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ranchaowen on 15-4-24.
 */
@Repository
public interface WordTypeDao extends JpaRepository<WordType, Long> {
    public WordType findByName(String name);
}
