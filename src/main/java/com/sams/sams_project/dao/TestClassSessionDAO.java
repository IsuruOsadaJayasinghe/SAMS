package com.sams.sams_project.dao;

import com.sams.sams_project.model.ClassSession;
import java.util.List;

public class TestClassSessionDAO {

    public static void main(String[] args) {

        ClassSessionDAO dao = new ClassSessionDAO();

        List<ClassSession> sessions = dao.getAllSessions();

        if (sessions.isEmpty()) {
            System.out.println("No class sessions found.");
            return;
        }

        System.out.println("Class sessions retrieved successfully:");
        System.out.println("--------------------------------------");

        for (ClassSession session : sessions) {
            System.out.println(
                    "Session ID: " + session.getSessionId()
                    + " | Course ID: " + session.getCourseId()
                    + " | Subject ID: " + session.getSubjectId()
                    + " | Lecturer ID: " + session.getLecturerId()
                    + " | Date: " + session.getClassDate()
                    + " | Start: " + session.getStartTime()
                    + " | End: " + session.getEndTime()
            );
        }
    }
}