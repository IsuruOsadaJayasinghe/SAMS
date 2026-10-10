package com.sams.sams_project.view;

import com.sams.sams_project.SAMS_Project;
import com.sams.sams_project.model.User;
import com.sams.sams_project.service.LoginService;

import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginView extends VBox {

    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();

    private final LoginService loginService = new LoginService();
    private final SAMS_Project application;

    public LoginView(SAMS_Project application) {
        this.application = application;

        setSpacing(12);
        setPadding(new Insets(30));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: #f4f7fb;");

        Label title = new Label(
                "Student Attendance Management System");

        title.setStyle(
                "-fx-font-size: 21px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #1f4e79;");

        Label subtitle = new Label(
                "Please log in to continue");

        subtitle.setStyle(
                "-fx-font-size: 13px;"
                + "-fx-text-fill: #555555;");

        Label usernameLabel = new Label("Username");
        usernameLabel.setStyle("-fx-font-weight: bold;");

        usernameField.setPromptText("Enter username");
        usernameField.setMaxWidth(280);

        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-font-weight: bold;");

        passwordField.setPromptText("Enter password");
        passwordField.setMaxWidth(280);

        Button loginButton = new Button("Login");
        loginButton.setDefaultButton(true);
        loginButton.setPrefWidth(150);
        loginButton.setPrefHeight(35);

        loginButton.setStyle(
                "-fx-background-color: #1f4e79;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-cursor: hand;");

        loginButton.setOnAction(event -> handleLogin());

        getChildren().addAll(
                title,
                subtitle,
                usernameLabel,
                usernameField,
                passwordLabel,
                passwordField,
                loginButton
        );

        setPrefSize(500, 350);
    }

    private void handleLogin() {

        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Validate empty input
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing Information",
                    "Please enter both username and password."
            );
            return;
        }

        try {
            User user = loginService.login(username, password);

            // Reject invalid credentials
            if (user == null) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Login Failed",
                        "Invalid username, password, or user role."
                );

                passwordField.clear();
                passwordField.requestFocus();
                return;
            }

            // Validate the user's role
            String role = user.getRole();

            if (role == null
                    || (!role.trim().equalsIgnoreCase("Admin")
                    && !role.trim().equalsIgnoreCase("Lecturer"))) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Access Denied",
                        "Your account does not have a permitted role."
                );
                passwordField.clear();
                return;
            }

            // Open the role-based dashboard
            application.showDashboard(user);

        } catch (SQLException e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Database Error",
                    "Unable to log in. Please check the database "
                    + "connection and configuration."
            );

            e.printStackTrace();
        } catch (RuntimeException e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Application Error",
                    "An unexpected error occurred during login."
            );

            e.printStackTrace();
        }
    }

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}