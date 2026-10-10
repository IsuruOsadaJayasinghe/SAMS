package com.sams.sams_project.dao;

import com.sams.sams_project.model.Lecturer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LecturerDAO {

    public boolean addLecturer(Lecturer lecturer) {
        String sql = "INSERT INTO lecturers "
                + "(lecturer_name, email, phone) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, lecturer.getLecturerName());
            statement.setString(2, lecturer.getEmail());
            statement.setString(3, lecturer.getPhone());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding lecturer: " + e.getMessage());
            return false;
        }
    }

    public List<Lecturer> getAllLecturers() {
        List<Lecturer> lecturers = new ArrayList<>();

        String sql = "SELECT lecturer_id, lecturer_name, email, phone "
                + "FROM lecturers ORDER BY lecturer_id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Lecturer lecturer = new Lecturer();

                lecturer.setLecturerId(result.getInt("lecturer_id"));
                lecturer.setLecturerName(result.getString("lecturer_name"));
                lecturer.setEmail(result.getString("email"));
                lecturer.setPhone(result.getString("phone"));

                lecturers.add(lecturer);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving lecturers: " + e.getMessage());
        }

        return lecturers;
    }

    public boolean updateLecturer(Lecturer lecturer) {
        String sql = "UPDATE lecturers SET lecturer_name = ?, "
                + "email = ?, phone = ? WHERE lecturer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, lecturer.getLecturerName());
            statement.setString(2, lecturer.getEmail());
            statement.setString(3, lecturer.getPhone());
            statement.setInt(4, lecturer.getLecturerId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating lecturer: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteLecturer(int lecturerId) {
        String sql = "DELETE FROM lecturers WHERE lecturer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, lecturerId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting lecturer: " + e.getMessage());
            return false;
        }
    }
}