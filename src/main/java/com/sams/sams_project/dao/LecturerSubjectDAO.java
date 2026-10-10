package com.sams.sams_project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LecturerSubjectDAO {

    public boolean assignSubject(int lecturerId, int subjectId) {
        String sql = "INSERT INTO lecturer_subjects "
                + "(lecturer_id, subject_id) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, lecturerId);
            statement.setInt(2, subjectId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error assigning subject: " + e.getMessage());
            return false;
        }
    }

    public List<Integer> getSubjectIdsForLecturer(int lecturerId) {
        List<Integer> subjectIds = new ArrayList<>();

        String sql = "SELECT subject_id FROM lecturer_subjects "
                + "WHERE lecturer_id = ? ORDER BY subject_id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, lecturerId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    subjectIds.add(result.getInt("subject_id"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving lecturer subjects: "
                    + e.getMessage());
        }

        return subjectIds;
    }

    public boolean removeSubjectAssignment(int lecturerId, int subjectId) {
        String sql = "DELETE FROM lecturer_subjects "
                + "WHERE lecturer_id = ? AND subject_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, lecturerId);
            statement.setInt(2, subjectId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error removing subject assignment: "
                    + e.getMessage());
            return false;
        }
    }

    public List<String> getAssignments() {
        List<String> assignments = new ArrayList<>();

        String sql = "SELECT l.lecturer_name, s.subject_name, s.subject_code "
                + "FROM lecturer_subjects ls "
                + "JOIN lecturers l ON ls.lecturer_id = l.lecturer_id "
                + "JOIN subjects s ON ls.subject_id = s.subject_id "
                + "ORDER BY l.lecturer_name, s.subject_name";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                String assignment = result.getString("lecturer_name")
                        + " - " + result.getString("subject_name")
                        + " (" + result.getString("subject_code") + ")";

                assignments.add(assignment);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving assignments: "
                    + e.getMessage());
        }

        return assignments;
    }
}