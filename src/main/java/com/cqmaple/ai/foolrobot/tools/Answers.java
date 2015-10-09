package com.cqmaple.ai.foolrobot.tools;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ranchaowen on 15/7/17.
 */
public class Answers {
    private Set<List<QuestionDTO>> questionDTOs;
    private Set<String> canBeSave;
    private Set<String> saved;
    private Set<String> htmls;
    public Answers() {
        this.questionDTOs = new HashSet<List<QuestionDTO>>();
        this.canBeSave=new HashSet<String>();
        this.saved=new HashSet<String>();
        this.htmls=new HashSet<String>();
    }

    public synchronized Set<List<QuestionDTO>> getQuestionDTOs() {
        return questionDTOs;
    }

    public synchronized  Set<String> getCanBeSave() {
        return canBeSave;
    }

    public synchronized Set<String> getSaved() {
        return saved;
    }

    public synchronized Set<String> getHtmls() {
        return htmls;
    }
}
