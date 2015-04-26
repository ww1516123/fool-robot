package com.cqmaple.ai.foolrobot.controller.vo;

/**
 * Created by Maple on 2015/4/26.
 */
public class WordTypeVo {
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
    private String parent;

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

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "WordTypeVo{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", parent='" + parent + '\'' +
                '}';
    }
}
