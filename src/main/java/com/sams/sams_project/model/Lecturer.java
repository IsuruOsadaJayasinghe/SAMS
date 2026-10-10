package com.sams.sams_project.model;

public class Lecturer {

    private int lecturerId;
    private String lecturerName;
    private String email;
    private String phone;

    public Lecturer() {
    }

    public Lecturer(int lecturerId, String lecturerName,
                    String email, String phone) {
        this.lecturerId = lecturerId;
        this.lecturerName = lecturerName;
        this.email = email;
        this.phone = phone;
    }

    public int getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(int lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
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
}