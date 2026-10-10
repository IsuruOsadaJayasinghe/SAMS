package com.sams.sams_project.dao;

import com.sams.sams_project.model.ClassSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClassSessionDAO {

    public boolean addSession(ClassSession session) {
        String sql = "INSERT INTO class_sessions "
                + "(course_id, subject_id, lecturer_id, class_date, "
                + "start_time, end_time) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, session.getCourseId());
            statement.setInt(2, session.getSubjectId());
            statement.setInt(3, session.getLecturerId());
            statement.setDate(4, java.sql.Date.valueOf(session.getClassDate()));
            statement.setTime(5, java.sql.Time.valueOf(session.getStartTime()));
            statement.setTime(6, java.sql.Time.valueOf(session.getEndTime()));

            return statement.executeUpdate() > 0;

        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Error adding class session: " + e.getMessage());
            return false;
        }
    }

    public List<ClassSession> getAllSessions() {
        List<ClassSession> sessions = new ArrayList<>();

        String sql = "SELECT session_id, course_id, subject_id, lecturer_id, "
                + "class_date, start_time, end_time "
                + "FROM class_sessions ORDER BY class_date, start_time";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                ClassSession session = new ClassSession();

                session.setSessionId(result.getInt("session_id"));
                session.setCourseId(result.getInt("course_id"));
                session.setSubjectId(result.getInt("subject_id"));
                session.setLecturerId(result.getInt("lecturer_id"));
                session.setClassDate(
                        result.getDate("class_date").toLocalDate());
                session.setStartTime(
                        result.getTime("start_time").toLocalTime());
                session.setEndTime(
                        result.getTime("end_time").toLocalTime());

                sessions.add(session);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving class sessions: "
                    + e.getMessage());
        }

        return sessions;
    }
    
    public List<ClassSession> getSessionsByLecturer(int lecturerId) {
    List<ClassSession> sessions = new ArrayList<>();

    String sql = "SELECT session_id, course_id, subject_id, lecturer_id, "
            + "class_date, start_time, end_time "
            + "FROM class_sessions "
            + "WHERE lecturer_id = ? "
            + "ORDER BY class_date, start_time";

    if (lecturerId <= 0) {
        return sessions;
    }

    try (Connection connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, lecturerId);

        try (ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                ClassSession session = new ClassSession();

                session.setSessionId(result.getInt("session_id"));
                session.setCourseId(result.getInt("course_id"));
                session.setSubjectId(result.getInt("subject_id"));
                session.setLecturerId(result.getInt("lecturer_id"));
                session.setClassDate(
                        result.getDate("class_date").toLocalDate());
                session.setStartTime(
                        result.getTime("start_time").toLocalTime());
                session.setEndTime(
                        result.getTime("end_time").toLocalTime());

                sessions.add(session);
            }
        }

    } catch (SQLException e) {
        System.out.println("Error retrieving lecturer sessions: "
                + e.getMessage());
    }

    return sessions;
}

    public boolean updateSession(ClassSession session) {
        String sql = "UPDATE class_sessions SET course_id = ?, "
                + "subject_id = ?, lecturer_id = ?, class_date = ?, "
                + "start_time = ?, end_time = ? WHERE session_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, session.getCourseId());
            statement.setInt(2, session.getSubjectId());
            statement.setInt(3, session.getLecturerId());
            statement.setDate(4, java.sql.Date.valueOf(session.getClassDate()));
            statement.setTime(5, java.sql.Time.valueOf(session.getStartTime()));
            statement.setTime(6, java.sql.Time.valueOf(session.getEndTime()));
            statement.setInt(7, session.getSessionId());

            return statement.executeUpdate() > 0;

        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Error updating class session: "
                    + e.getMessage());
            return false;
        }
    }

    public boolean deleteSession(int sessionId) {
        String sql = "DELETE FROM class_sessions WHERE session_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, sessionId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting class session: "
                    + e.getMessage());
            return false;
        }
    }
}