package com.scnu.sihao.orienteering.Entity;

import java.util.List;

/**
 * Created by SiHao on 2017/12/15.
 */

public class GetAQDataBean {

    private List<Boolean> Answer;
    private List<String> Question;

    public List<Boolean> getAnswer() {
        return Answer;
    }

    public void setAnswer(List<Boolean> Answer) {
        this.Answer = Answer;
    }

    public List<String> getQuestion() {
        return Question;
    }

    public void setQuestion(List<String> Question) {
        this.Question = Question;
    }
}
