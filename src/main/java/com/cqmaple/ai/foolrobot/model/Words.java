package com.cqmaple.ai.foolrobot.model;


import com.cqmaple.ai.foolrobot.common.BaseEntity;

import javax.persistence.*;

/**
 * Created by ranchaowen on 15-4-16.
 */
@Entity
public class Words extends BaseEntity{
    /**
     * 词语
     */
    @Column(length = 20)
    private String words;
    @Column(length = 40)
    private String eWords;
    @ManyToOne
    @JoinColumn(name = "t_id")
    private WordType type;

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String geteWords() {
        return eWords;
    }

    public void seteWords(String eWords) {
        this.eWords = eWords;
    }

    public WordType getType() {
        return type;
    }

    public void setType(WordType type) {
        this.type = type;
    }
}
