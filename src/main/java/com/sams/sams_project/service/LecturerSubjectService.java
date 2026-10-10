package com.sams.sams_project.service;

import com.sams.sams_project.dao.LecturerSubjectDAO;
import java.util.List;

public class LecturerSubjectService {

    private final LecturerSubjectDAO assignmentDAO =
            new LecturerSubjectDAO();

    public boolean assignSubject(int lecturerId, int subjectId) {
        if (lecturerId <= 0 || subjectId <= 0) {
            return false;
        }

        return assignmentDAO.assignSubject(lecturerId, subjectId);
    }

    public List<Integer> getSubjectIdsForLecturer(int lecturerId) {
        if (lecturerId <= 0) {
            return List.of();
        }

        return assignmentDAO.getSubjectIdsForLecturer(lecturerId);
    }

    public boolean removeSubjectAssignment(int lecturerId, int subjectId) {
        if (lecturerId <= 0 || subjectId <= 0) {
            return false;
        }

        return assignmentDAO.removeSubjectAssignment(lecturerId, subjectId);
    }

    public List<String> getAssignments() {
        return assignmentDAO.getAssignments();
    }
}