package com.cqmaple.ai.foolrobot.controller.vo;

import com.cqmaple.ai.foolrobot.model.WordType;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by ranchaowen on 15/7/12.
 */
public class WordVo {
    private String id;
    private String words;
    private String eWords;
    private String type_id;

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

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }
}
