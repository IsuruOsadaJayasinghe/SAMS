package com.sams.sams_project.service;

import com.sams.sams_project.dao.UserDAO;
import com.sams.sams_project.model.User;
import java.sql.SQLException;

public class LoginService {

    private final UserDAO userDAO = new UserDAO();

    public User login(String username, String password)
            throws SQLException {

        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {
            return null;
        }

        User user = userDAO.login(username.trim(), password);

        if (user == null) {
            return null;
        }

        String role = user.getRole();

        if (role == null
                || (!role.equalsIgnoreCase("Admin")
                && !role.equalsIgnoreCase("Lecturer"))) {
            return null;
        }

        return user;
    }
}