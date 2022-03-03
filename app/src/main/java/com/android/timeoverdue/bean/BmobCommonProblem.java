package com.android.timeoverdue.bean;

import cn.bmob.v3.BmobObject;

public class BmobCommonProblem extends BmobObject {

    /**
     * 问题
     */
    private String problem;

    /**
     * 回答
     */
    private String answer;

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
