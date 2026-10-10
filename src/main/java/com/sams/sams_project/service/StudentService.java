package com.sams.sams_project.service;

import com.sams.sams_project.dao.StudentDAO;
import com.sams.sams_project.model.Student;
import java.util.List;

public class StudentService {

    private final StudentDAO studentDAO = new StudentDAO();

    public boolean addStudent(Student student) {
        if (student == null
                || student.getRegistrationNo() == null
                || student.getRegistrationNo().trim().isEmpty()
                || student.getStudentName() == null
                || student.getStudentName().trim().isEmpty()
                || student.getCourseId() <= 0) {
            return false;
        }

        student.setRegistrationNo(student.getRegistrationNo().trim());
        student.setStudentName(student.getStudentName().trim());

        if (student.getEmail() != null) {
            student.setEmail(student.getEmail().trim());
        }

        if (student.getPhone() != null) {
            student.setPhone(student.getPhone().trim());
        }

        return studentDAO.addStudent(student);
    }

    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    public boolean updateStudent(Student student) {
        if (student == null
                || student.getStudentId() <= 0
                || student.getRegistrationNo() == null
                || student.getRegistrationNo().trim().isEmpty()
                || student.getStudentName() == null
                || student.getStudentName().trim().isEmpty()
                || student.getCourseId() <= 0) {
            return false;
        }

        student.setRegistrationNo(student.getRegistrationNo().trim());
        student.setStudentName(student.getStudentName().trim());

        if (student.getEmail() != null) {
            student.setEmail(student.getEmail().trim());
        }

        if (student.getPhone() != null) {
            student.setPhone(student.getPhone().trim());
        }

        return studentDAO.updateStudent(student);
    }

    public boolean deleteStudent(int studentId) {
        if (studentId <= 0) {
            return false;
        }

        return studentDAO.deleteStudent(studentId);
    }
}