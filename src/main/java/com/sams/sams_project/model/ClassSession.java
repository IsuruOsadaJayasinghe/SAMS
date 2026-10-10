package com.sams.sams_project.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class ClassSession {

    private int sessionId;
    private int courseId;
    private int subjectId;
    private int lecturerId;
    private LocalDate classDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public ClassSession() {
    }

    public ClassSession(int sessionId, int courseId,
                        int subjectId, int lecturerId,
                        LocalDate classDate, LocalTime startTime,
                        LocalTime endTime) {
        this.sessionId = sessionId;
        this.courseId = courseId;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.classDate = classDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(int lecturerId) {
        this.lecturerId = lecturerId;
    }

    public LocalDate getClassDate() {
        return classDate;
    }

    public void setClassDate(LocalDate classDate) {
        this.classDate = classDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}