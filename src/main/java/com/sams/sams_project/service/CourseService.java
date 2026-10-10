package com.sams.sams_project.service;

import com.sams.sams_project.dao.CourseDAO;
import com.sams.sams_project.model.Course;
import java.util.List;

public class CourseService {

    private final CourseDAO courseDAO = new CourseDAO();

    public boolean addCourse(Course course) {
        if (course.getCourseName() == null
                || course.getCourseName().trim().isEmpty()) {
            return false;
        }

        if (course.getCourseCode() == null
                || course.getCourseCode().trim().isEmpty()) {
            return false;
        }

        course.setCourseName(course.getCourseName().trim());
        course.setCourseCode(course.getCourseCode().trim());

        return courseDAO.addCourse(course);
    }

    public List<Course> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    public boolean updateCourse(Course course) {
        if (course == null || course.getCourseId() <= 0) {
            return false;
        }

        if (course.getCourseName() == null
                || course.getCourseName().trim().isEmpty()) {
            return false;
        }

        if (course.getCourseCode() == null
                || course.getCourseCode().trim().isEmpty()) {
            return false;
        }

        course.setCourseName(course.getCourseName().trim());
        course.setCourseCode(course.getCourseCode().trim());

        return courseDAO.updateCourse(course);
    }

    public boolean deleteCourse(int courseId) {
        if (courseId <= 0) {
            return false;
        }

        return courseDAO.deleteCourse(courseId);
    }
}