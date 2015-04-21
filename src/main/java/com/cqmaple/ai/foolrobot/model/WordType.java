package com.cqmaple.ai.foolrobot.model;

import com.cqmaple.ai.foolrobot.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by ranchaowen on 15-4-16.
 */
@Entity
public class WordType extends BaseEntity{

    /**
     * 类型名称
     */
    @Column(length = 10)
    private String name;
    /**
     * 解释
     */
    @Column
    private String description;

    @ManyToOne
    private WordType parent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WordType getParent() {
        return parent;
    }

    public void setParent(WordType parent) {
        this.parent = parent;
    }
}
