package com.sams.sams_project.dao;

import com.sams.sams_project.model.Subject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {

    public boolean addSubject(Subject subject) {
        String sql = "INSERT INTO subjects "
                + "(subject_name, subject_code, course_id) "
                + "VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, subject.getSubjectName());
            statement.setString(2, subject.getSubjectCode());
            statement.setInt(3, subject.getCourseId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding subject: " + e.getMessage());
            return false;
        }
    }

    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();

        String sql = "SELECT subject_id, subject_name, subject_code, course_id "
                + "FROM subjects ORDER BY subject_id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Subject subject = new Subject();

                subject.setSubjectId(result.getInt("subject_id"));
                subject.setSubjectName(result.getString("subject_name"));
                subject.setSubjectCode(result.getString("subject_code"));
                subject.setCourseId(result.getInt("course_id"));

                subjects.add(subject);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving subjects: " + e.getMessage());
        }

        return subjects;
    }

    public boolean updateSubject(Subject subject) {
        String sql = "UPDATE subjects SET subject_name = ?, "
                + "subject_code = ?, course_id = ? WHERE subject_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, subject.getSubjectName());
            statement.setString(2, subject.getSubjectCode());
            statement.setInt(3, subject.getCourseId());
            statement.setInt(4, subject.getSubjectId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating subject: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteSubject(int subjectId) {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, subjectId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting subject: " + e.getMessage());
            return false;
        }
    }
}