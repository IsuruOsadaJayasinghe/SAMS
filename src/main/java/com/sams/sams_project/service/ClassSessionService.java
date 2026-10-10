package com.sams.sams_project.service;

import com.sams.sams_project.dao.ClassSessionDAO;
import com.sams.sams_project.model.ClassSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ClassSessionService {

    private final ClassSessionDAO sessionDAO = new ClassSessionDAO();

    public boolean addSession(ClassSession session) {
        if (!isValidSession(session)) {
            return false;
        }

        return sessionDAO.addSession(session);
    }

    public List<ClassSession> getAllSessions() {
        return sessionDAO.getAllSessions();
    }

    public boolean updateSession(ClassSession session) {
        if (session == null
                || session.getSessionId() <= 0
                || !isValidSession(session)) {
            return false;
        }

        return sessionDAO.updateSession(session);
    }

    public boolean deleteSession(int sessionId) {
        if (sessionId <= 0) {
            return false;
        }

        return sessionDAO.deleteSession(sessionId);
    }

    private boolean isValidSession(ClassSession session) {
        if (session == null
                || session.getCourseId() <= 0
                || session.getSubjectId() <= 0
                || session.getLecturerId() <= 0
                || session.getClassDate() == null
                || session.getStartTime() == null
                || session.getEndTime() == null) {
            return false;
        }

        LocalDate classDate = session.getClassDate();
        LocalTime startTime = session.getStartTime();
        LocalTime endTime = session.getEndTime();

        return !classDate.isBefore(LocalDate.now())
                && endTime.isAfter(startTime);
    }
}