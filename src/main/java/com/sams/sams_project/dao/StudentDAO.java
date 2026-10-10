package com.sams.sams_project.dao;

import com.sams.sams_project.model.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students "
                + "(registration_no, student_name, email, phone, course_id) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getRegistrationNo());
            statement.setString(2, student.getStudentName());
            statement.setString(3, student.getEmail());
            statement.setString(4, student.getPhone());
            statement.setInt(5, student.getCourseId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
            return false;
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        String sql = "SELECT student_id, registration_no, student_name, "
                + "email, phone, course_id FROM students "
                + "ORDER BY student_id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Student student = new Student();

                student.setStudentId(result.getInt("student_id"));
                student.setRegistrationNo(result.getString("registration_no"));
                student.setStudentName(result.getString("student_name"));
                student.setEmail(result.getString("email"));
                student.setPhone(result.getString("phone"));
                student.setCourseId(result.getInt("course_id"));

                students.add(student);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving students: " + e.getMessage());
        }

        return students;
    }

    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET registration_no = ?, "
                + "student_name = ?, email = ?, phone = ?, course_id = ? "
                + "WHERE student_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getRegistrationNo());
            statement.setString(2, student.getStudentName());
            statement.setString(3, student.getEmail());
            statement.setString(4, student.getPhone());
            statement.setInt(5, student.getCourseId());
            statement.setInt(6, student.getStudentId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }
}