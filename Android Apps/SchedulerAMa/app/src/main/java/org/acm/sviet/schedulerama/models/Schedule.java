package org.acm.sviet.schedulerama.models;

/**
 * Created by Anurag on 28-01-2016.
 *
 * Basic Model used by the Schedule Activity for generating a ListView of Schedules.
 *
 */
public class Schedule {
    private int weekDay;
    private int lectureNo;
    private Course course;

    //getters & setters...


    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public int getLectureNo() {
        return lectureNo;
    }

    public void setLectureNo(int lectureNo) {
        this.lectureNo = lectureNo;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }



    //Chained Methods..

    public Schedule addWeekDay(int weekDay) {
        this.weekDay = weekDay;
        return this;
    }

    public Schedule addLectureNo(int lectureNo) {
        this.lectureNo = lectureNo;
        return this;
    }

    public Schedule addCourse(Course course) {
        this.course = course;
        return this;
    }

}
