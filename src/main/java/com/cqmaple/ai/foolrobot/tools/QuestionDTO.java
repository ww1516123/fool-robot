package com.cqmaple.ai.foolrobot.tools;

/**
 * Created by ranchaowen on 15/7/12.
 */
public class QuestionDTO {
    private String question;
    private String answer;
    private String time;
    private String who;
    private String how;
    private String moreHref;
    private String sart;

    public QuestionDTO(String question, String answer, String time, String who, String how, String moreHref, String sart) {
        this.question = question;
        this.answer = answer;
        this.time = time;
        this.who = who;
        this.how = how;
        this.moreHref = moreHref;
        this.sart = sart;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getHow() {
        return how;
    }

    public void setHow(String how) {
        this.how = how;
    }

    public String getMoreHref() {
        return moreHref;
    }

    public void setMoreHref(String moreHref) {
        this.moreHref = moreHref;
    }

    public String getSart() {
        if(sart==null||sart.equals("")){
            sart="0";
        }
        return sart;
    }

    public void setSart(String sart) {
        this.sart = sart;
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", time='" + time + '\'' +
                ", who='" + who + '\'' +
                ", how='" + how + '\'' +
                ", moreHref='" + moreHref + '\'' +
                ", sart='" + sart + '\'' +
                '}';
    }
}
