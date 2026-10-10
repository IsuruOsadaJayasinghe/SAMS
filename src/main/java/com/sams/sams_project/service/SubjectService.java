package com.sams.sams_project.service;

import com.sams.sams_project.dao.SubjectDAO;
import com.sams.sams_project.model.Subject;
import java.util.List;

public class SubjectService {

    private final SubjectDAO subjectDAO = new SubjectDAO();

    public boolean addSubject(Subject subject) {
        if (subject == null
                || subject.getSubjectName() == null
                || subject.getSubjectName().trim().isEmpty()
                || subject.getSubjectCode() == null
                || subject.getSubjectCode().trim().isEmpty()
                || subject.getCourseId() <= 0) {
            return false;
        }

        subject.setSubjectName(subject.getSubjectName().trim());
        subject.setSubjectCode(subject.getSubjectCode().trim());

        return subjectDAO.addSubject(subject);
    }

    public List<Subject> getAllSubjects() {
        return subjectDAO.getAllSubjects();
    }

    public boolean updateSubject(Subject subject) {
        if (subject == null
                || subject.getSubjectId() <= 0
                || subject.getSubjectName() == null
                || subject.getSubjectName().trim().isEmpty()
                || subject.getSubjectCode() == null
                || subject.getSubjectCode().trim().isEmpty()
                || subject.getCourseId() <= 0) {
            return false;
        }

        subject.setSubjectName(subject.getSubjectName().trim());
        subject.setSubjectCode(subject.getSubjectCode().trim());

        return subjectDAO.updateSubject(subject);
    }

    public boolean deleteSubject(int subjectId) {
        if (subjectId <= 0) {
            return false;
        }

        return subjectDAO.deleteSubject(subjectId);
    }
}