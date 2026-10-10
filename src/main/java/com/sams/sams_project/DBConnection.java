package com.sams.sams_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/sams_db";

    private static final String USER = "root";

    public static Connection getConnection() throws SQLException {

        String password = System.getenv("SAMS_DB_PASSWORD");

        if (password == null || password.isBlank()) {
            throw new SQLException(
                    "SAMS_DB_PASSWORD environment variable is not set."
            );
        }

        return DriverManager.getConnection(URL, USER, password);
    }
}