package com.sams.sams_project.dao;

import com.sams.sams_project.DBConnection;
import com.sams.sams_project.model.Attendance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    public boolean addAttendance(Attendance attendance) {
        String sql = "INSERT INTO attendance "
                + "(session_id, student_id, status) "
                + "VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, attendance.getSessionId());
            ps.setInt(2, attendance.getStudentId());
            ps.setString(3, attendance.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Attendance> getAllAttendance() {
        List<Attendance> list = new ArrayList<>();

        String sql = "SELECT attendance_id, session_id, "
                + "student_id, status FROM attendance";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Attendance attendance = new Attendance();

                attendance.setAttendanceId(
                        rs.getInt("attendance_id"));
                attendance.setSessionId(
                        rs.getInt("session_id"));
                attendance.setStudentId(
                        rs.getInt("student_id"));
                attendance.setStatus(
                        rs.getString("status"));

                list.add(attendance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateAttendance(Attendance attendance) {
        String sql = "UPDATE attendance SET session_id = ?, "
                + "student_id = ?, status = ? "
                + "WHERE attendance_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, attendance.getSessionId());
            ps.setInt(2, attendance.getStudentId());
            ps.setString(3, attendance.getStatus());
            ps.setInt(4, attendance.getAttendanceId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAttendance(int attendanceId) {
        String sql = "DELETE FROM attendance "
                + "WHERE attendance_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, attendanceId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<java.util.Map<String, Object>> getAttendanceReport(
        Integer studentId,
        Integer courseId,
        Integer subjectId,
        java.time.LocalDate startDate,
        java.time.LocalDate endDate) {

    List<java.util.Map<String, Object>> report =
            new ArrayList<>();

    StringBuilder sql = new StringBuilder(
            "SELECT a.attendance_id, "
            + "s.student_id, s.registration_no, s.student_name, "
            + "c.course_name, sub.subject_name, "
            + "cs.class_date, cs.start_time, cs.end_time, "
            + "a.status "
            + "FROM attendance a "
            + "JOIN students s ON a.student_id = s.student_id "
            + "JOIN class_sessions cs ON a.session_id = cs.session_id "
            + "JOIN courses c ON cs.course_id = c.course_id "
            + "JOIN subjects sub ON cs.subject_id = sub.subject_id "
            + "WHERE 1 = 1 ");

    List<Object> params = new ArrayList<>();

    if (studentId != null && studentId > 0) {
        sql.append("AND s.student_id = ? ");
        params.add(studentId);
    }

    if (courseId != null && courseId > 0) {
        sql.append("AND c.course_id = ? ");
        params.add(courseId);
    }

    if (subjectId != null && subjectId > 0) {
        sql.append("AND sub.subject_id = ? ");
        params.add(subjectId);
    }

    if (startDate != null) {
        sql.append("AND cs.class_date >= ? ");
        params.add(java.sql.Date.valueOf(startDate));
    }

    if (endDate != null) {
        sql.append("AND cs.class_date <= ? ");
        params.add(java.sql.Date.valueOf(endDate));
    }

    sql.append("ORDER BY cs.class_date, cs.start_time, s.student_name");

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql.toString())) {

        for (int i = 0; i < params.size(); i++) {
            Object value = params.get(i);

            if (value instanceof Integer) {
                ps.setInt(i + 1, (Integer) value);
            } else {
                ps.setDate(i + 1, (java.sql.Date) value);
            }
        }

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                java.util.Map<String, Object> row =
                        new java.util.LinkedHashMap<>();

                row.put("attendanceId",
                        rs.getInt("attendance_id"));
                row.put("studentId",
                        rs.getInt("student_id"));
                row.put("registrationNo",
                        rs.getString("registration_no"));
                row.put("studentName",
                        rs.getString("student_name"));
                row.put("courseName",
                        rs.getString("course_name"));
                row.put("subjectName",
                        rs.getString("subject_name"));
                row.put("classDate",
                        rs.getDate("class_date").toLocalDate());
                row.put("startTime",
                        rs.getTime("start_time").toLocalTime());
                row.put("endTime",
                        rs.getTime("end_time").toLocalTime());
                row.put("status",
                        rs.getString("status"));

                report.add(row);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return report;
}
}