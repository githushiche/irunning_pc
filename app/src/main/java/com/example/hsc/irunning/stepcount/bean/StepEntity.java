package com.example.hsc.irunning.stepcount.bean;

/**
 * 计步实体
 *
 * @author Diviner
 * @date 2018-5-20 下午4:52:08
 */
public class StepEntity {
    private String curDate; // 当天的日期
    private String steps; // 当天的步数

    public StepEntity() {
    }

    public StepEntity(String curDate, String steps) {
        this.curDate = curDate;
        this.steps = steps;
    }

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "StepEntity{" + "curDate='" + curDate + '\'' + ", steps="
                + steps + '}';
    }
}
