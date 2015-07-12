package com.cqmaple.ai.foolrobot.controller.vo;

import com.cqmaple.ai.foolrobot.common.BaseVo;
import com.cqmaple.ai.foolrobot.model.WordType;

/**
 * Created by Maple on 2015/4/26.
 */
public class WordTypeVo extends BaseVo{

    public WordTypeVo(){

    }
    public WordTypeVo(WordType wordType){
        this.name=wordType.getName();
        this.description=wordType.getDescription();
        this.parentName=wordType.getParent().getName();
        this.setId(wordType.getId());
    }
    /**
     * 类型名称
     */
    private String name;
    /**
     * 解释
     */
    private String description;
    /**
     * 上级
     */
    private String parentName;

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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public String toString() {
        return "WordTypeVo{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", parent='" + parentName + '\'' +
                '}';
    }
}
