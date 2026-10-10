package com.sams.sams_project.service;

import com.sams.sams_project.dao.AttendanceDAO;
import com.sams.sams_project.model.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AttendanceService {

    private final AttendanceDAO attendanceDAO =
            new AttendanceDAO();

    // CREATE
    public boolean addAttendance(Attendance attendance) {
        if (!isValidAttendance(attendance)) {
            return false;
        }

        return attendanceDAO.addAttendance(attendance);
    }

    // READ ALL
    public List<Attendance> getAllAttendance() {
        return attendanceDAO.getAllAttendance();
    }

    // UPDATE
    public boolean updateAttendance(Attendance attendance) {
        if (attendance == null
                || attendance.getAttendanceId() <= 0
                || !isValidAttendance(attendance)) {
            return false;
        }

        return attendanceDAO.updateAttendance(attendance);
    }

    // DELETE
    public boolean deleteAttendance(int attendanceId) {
        if (attendanceId <= 0) {
            return false;
        }

        return attendanceDAO.deleteAttendance(attendanceId);
    }

    // ATTENDANCE REPORT
    public List<Map<String, Object>> getAttendanceReport(
            Integer studentId,
            Integer courseId,
            Integer subjectId,
            LocalDate startDate,
            LocalDate endDate) {

        if (startDate != null && endDate != null
                && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date.");
        }

        return attendanceDAO.getAttendanceReport(
                studentId,
                courseId,
                subjectId,
                startDate,
                endDate);
    }

    // VALIDATION
    private boolean isValidAttendance(Attendance attendance) {
        if (attendance == null
                || attendance.getSessionId() <= 0
                || attendance.getStudentId() <= 0
                || attendance.getStatus() == null) {
            return false;
        }

        String status = attendance.getStatus().trim();

        return status.equalsIgnoreCase("Present")
                || status.equalsIgnoreCase("Absent")
                || status.equalsIgnoreCase("Late");
    }
}