package com.sams.sams_project;

import com.sams.sams_project.model.User;
import com.sams.sams_project.view.AttendanceReportView;
import com.sams.sams_project.view.AttendanceView;
import com.sams.sams_project.view.ClassSessionView;
import com.sams.sams_project.view.CourseView;
import com.sams.sams_project.view.LecturerView;
import com.sams.sams_project.view.LoginView;
import com.sams.sams_project.view.StudentView;
import com.sams.sams_project.view.SubjectView;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SAMS_Project extends Application {

    private Stage mainStage;
    private BorderPane root;
    private VBox dashboardContent;
    private User loggedInUser;

    @Override
    public void start(Stage stage) {
        mainStage = stage;

        mainStage.setTitle(
                "SAMS - Student Attendance Management System");

        showLogin();
    }

    // Display the login screen first
    private void showLogin() {
        LoginView loginView = new LoginView(this);

        Scene scene = new Scene(loginView, 500, 350);

        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();
    }

    // Called after successful authentication
    public void showDashboard(User user) {

        if (user == null || user.getRole() == null) {
            showMessage(
                    "Access Denied",
                    "A valid user and role are required.");
            return;
        }

        String role = user.getRole().trim();

        if (!role.equalsIgnoreCase("Admin")
                && !role.equalsIgnoreCase("Lecturer")) {

            showMessage(
                    "Access Denied",
                    "This user role is not permitted.");
            return;
        }

        loggedInUser = user;

        root = new BorderPane();

        // Dashboard title
        Label title = new Label(
                "Student Attendance Management System");

        title.setStyle(
                "-fx-font-size: 25px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: white;");

        // Logged-in user information
        Label userLabel = new Label(
                "Logged in: " + user.getUsername()
                + " | Role: " + role);

        userLabel.setStyle(
                "-fx-font-size: 13px;"
                + "-fx-text-fill: white;");

        // Logout button
        Button logoutButton = createButton("Logout");
        logoutButton.setPrefWidth(100);
        logoutButton.setPrefHeight(35);
        logoutButton.setOnAction(e -> logout());

        VBox header = new VBox(
                10, title, userLabel, logoutButton);

        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #1f4e79;");

        // Dashboard welcome section
        Label welcome = new Label(
                "Welcome, " + user.getUsername());

        welcome.setStyle(
                "-fx-font-size: 22px;"
                + "-fx-font-weight: bold;");

        Label description = new Label(
                role.equalsIgnoreCase("Admin")
                ? "Administrator Dashboard"
                : "Lecturer Dashboard");

        description.setStyle("-fx-font-size: 14px;");

        // Dashboard module layout
        GridPane menu = new GridPane();

        menu.setAlignment(Pos.CENTER);
        menu.setHgap(15);
        menu.setVgap(15);

        int row = 0;

        // Administrator-only modules
        if (role.equalsIgnoreCase("Admin")) {

            addModule(
                    menu,
                    "Student Management",
                    new StudentView(),
                    0,
                    row);

            addModule(
                    menu,
                    "Course Management",
                    new CourseView(),
                    1,
                    row);

            row++;

            addModule(
                    menu,
                    "Subject Management",
                    new SubjectView(),
                    0,
                    row);

            addModule(
                    menu,
                    "Lecturer Management",
                    new LecturerView(),
                    1,
                    row);

            row++;
        }

        // Class scheduling and attendance
        addModule(
                menu,
                "Class Scheduling",
                new ClassSessionView(),
                0,
                row);

        addModule(
                menu,
                "Attendance Management",
                new AttendanceView(),
                1,
                row);

        row++;

        // Attendance reports: Admin only
        if (role.equalsIgnoreCase("Admin")) {
            addModule(
                    menu,
                    "Attendance Reports",
                    new AttendanceReportView(),
                    0,
                    row);
        }

        // Dashboard content
        dashboardContent = new VBox(
                20,
                welcome,
                description,
                menu);

        dashboardContent.setAlignment(Pos.CENTER);
        dashboardContent.setPadding(new Insets(25));

        root.setTop(header);
        root.setCenter(dashboardContent);

        // Display the dashboard
        Scene scene = new Scene(root, 1000, 750);

        mainStage.setScene(scene);
        mainStage.setResizable(true);
        mainStage.setMinWidth(800);
        mainStage.setMinHeight(650);
        mainStage.show();
    }

    // Add a module button to the dashboard
    private void addModule(
            GridPane menu,
            String title,
            Node view,
            int column,
            int row) {

        Button button = createButton(title);

        button.setOnAction(e -> openView(view));

        menu.add(button, column, row);
    }

    // Create consistently styled buttons
    private Button createButton(String text) {

        Button button = new Button(text);

        button.setPrefWidth(240);
        button.setPrefHeight(60);

        button.setStyle(
                "-fx-font-size: 15px;"
                + "-fx-background-color: #e8f0f8;"
                + "-fx-background-radius: 8;"
                + "-fx-cursor: hand;");

        return button;
    }

    // Open a module with a Back to Dashboard button
    private void openView(Node view) {

        if (loggedInUser == null) {
            showMessage(
                    "Access Denied",
                    "Please log in first.");

            showLogin();
            return;
        }

        Button backButton =
                createButton("Back to Dashboard");

        backButton.setPrefHeight(40);

        backButton.setOnAction(
                e -> showDashboard(loggedInUser));

        VBox page = new VBox(
                15,
                backButton,
                view);

        page.setPadding(new Insets(15));
        page.setFillWidth(true);

        // Allow large modules and tables to scroll
        ScrollPane scrollPane = new ScrollPane(page);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setPannable(true);

        root.setCenter(scrollPane);
    }

    // Logout and return to login screen
    private void logout() {
        loggedInUser = null;
        showLogin();
    }

    // Display an information or error message
    private void showMessage(
            String title,
            String message) {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}