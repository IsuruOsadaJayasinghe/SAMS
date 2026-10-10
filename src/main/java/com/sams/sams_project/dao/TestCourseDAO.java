package com.sams.sams_project.dao;

import com.sams.sams_project.model.Course;

public class TestCourseDAO {

    public static void main(String[] args) {
        CourseDAO courseDAO = new CourseDAO();

        for (Course course : courseDAO.getAllCourses()) {
            System.out.println(
                    course.getCourseId() + " - "
                    + course.getCourseName() + " ("
                    + course.getCourseCode() + ")"
            );
        }
    }
}