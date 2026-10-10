package com.sams.sams_project.service;

import com.sams.sams_project.dao.LecturerDAO;
import com.sams.sams_project.model.Lecturer;
import java.util.List;

public class LecturerService {

    private final LecturerDAO lecturerDAO = new LecturerDAO();

    public boolean addLecturer(Lecturer lecturer) {
        if (lecturer == null
                || lecturer.getLecturerName() == null
                || lecturer.getLecturerName().trim().isEmpty()) {
            return false;
        }

        lecturer.setLecturerName(lecturer.getLecturerName().trim());

        if (lecturer.getEmail() != null) {
            lecturer.setEmail(lecturer.getEmail().trim());
        }

        if (lecturer.getPhone() != null) {
            lecturer.setPhone(lecturer.getPhone().trim());
        }

        return lecturerDAO.addLecturer(lecturer);
    }

    public List<Lecturer> getAllLecturers() {
        return lecturerDAO.getAllLecturers();
    }

    public boolean updateLecturer(Lecturer lecturer) {
        if (lecturer == null
                || lecturer.getLecturerId() <= 0
                || lecturer.getLecturerName() == null
                || lecturer.getLecturerName().trim().isEmpty()) {
            return false;
        }

        lecturer.setLecturerName(lecturer.getLecturerName().trim());

        if (lecturer.getEmail() != null) {
            lecturer.setEmail(lecturer.getEmail().trim());
        }

        if (lecturer.getPhone() != null) {
            lecturer.setPhone(lecturer.getPhone().trim());
        }

        return lecturerDAO.updateLecturer(lecturer);
    }

    public boolean deleteLecturer(int lecturerId) {
        if (lecturerId <= 0) {
            return false;
        }

        return lecturerDAO.deleteLecturer(lecturerId);
    }
}