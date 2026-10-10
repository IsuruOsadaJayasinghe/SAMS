package com.sams.sams_project.model;

public class Student {

    private int studentId;
    private String registrationNo;
    private String studentName;
    private String email;
    private String phone;
    private int courseId;

    public Student() {
    }

    public Student(int studentId, String registrationNo,
                   String studentName, String email,
                   String phone, int courseId) {
        this.studentId = studentId;
        this.registrationNo = registrationNo;
        this.studentName = studentName;
        this.email = email;
        this.phone = phone;
        this.courseId = courseId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}