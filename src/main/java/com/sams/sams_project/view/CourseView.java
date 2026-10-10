package com.sams.sams_project.view;

import com.sams.sams_project.model.Course;
import com.sams.sams_project.service.CourseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CourseView extends VBox {

    private final CourseService courseService = new CourseService();

    private final TableView<Course> courseTable = new TableView<>();
    private final TextField nameField = new TextField();
    private final TextField codeField = new TextField();

    public CourseView() {
        setSpacing(10);
        setPadding(new Insets(15));

        Label title = new Label("Course Management");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        nameField.setPromptText("Course name");
        codeField.setPromptText("Course code");

        TableColumn<Course, Integer> idColumn =
                new TableColumn<>("ID");
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("courseId"));

        TableColumn<Course, String> nameColumn =
                new TableColumn<>("Course Name");
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("courseName"));

        TableColumn<Course, String> codeColumn =
                new TableColumn<>("Course Code");
        codeColumn.setCellValueFactory(
                new PropertyValueFactory<>("courseCode"));

        courseTable.getColumns().addAll(
                idColumn, nameColumn, codeColumn);

        courseTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY);

        courseTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldCourse, selectedCourse) -> {
                    if (selectedCourse != null) {
                        nameField.setText(selectedCourse.getCourseName());
                        codeField.setText(selectedCourse.getCourseCode());
                    }
                });

        Button addButton = new Button("Add Course");
        Button updateButton = new Button("Update Course");
        Button deleteButton = new Button("Delete Course");
        Button clearButton = new Button("Clear");
        Button refreshButton = new Button("Refresh");

        addButton.setOnAction(e -> addCourse());
        updateButton.setOnAction(e -> updateCourse());
        deleteButton.setOnAction(e -> deleteCourse());
        clearButton.setOnAction(e -> clearFields());
        refreshButton.setOnAction(e -> loadCourses());

        HBox fields = new HBox(10, nameField, codeField);
        HBox buttons = new HBox(
                10, addButton, updateButton,
                deleteButton, clearButton, refreshButton);

        getChildren().addAll(title, fields, buttons, courseTable);

        loadCourses();
    }

    private void addCourse() {
        Course course = new Course();
        course.setCourseName(nameField.getText());
        course.setCourseCode(codeField.getText());

        if (courseService.addCourse(course)) {
            showMessage("Success", "Course added successfully.");
            clearFields();
            loadCourses();
        } else {
            showMessage("Error",
                    "Course was not added. Check the name, code, and database.");
        }
    }

    private void updateCourse() {
        Course selected = courseTable.getSelectionModel()
                .getSelectedItem();

        if (selected == null) {
            showMessage("Warning", "Please select a course first.");
            return;
        }

        selected.setCourseName(nameField.getText());
        selected.setCourseCode(codeField.getText());

        if (courseService.updateCourse(selected)) {
            showMessage("Success", "Course updated successfully.");
            clearFields();
            loadCourses();
        } else {
            showMessage("Error",
                    "Course was not updated. Check the entered details.");
        }
    }

    private void deleteCourse() {
        Course selected = courseTable.getSelectionModel()
                .getSelectedItem();

        if (selected == null) {
            showMessage("Warning", "Please select a course first.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Delete");
        confirmation.setHeaderText("Delete " + selected.getCourseName() + "?");
        confirmation.setContentText(
                "Courses linked to subjects or other records may not be deletable.");

        java.util.Optional<javafx.scene.control.ButtonType> result =
                confirmation.showAndWait();

        if (result.isPresent()
                && result.get() == javafx.scene.control.ButtonType.OK) {
            if (courseService.deleteCourse(selected.getCourseId())) {
                showMessage("Success", "Course deleted successfully.");
                clearFields();
                loadCourses();
            } else {
                showMessage("Error",
                        "Course could not be deleted. It may be linked to other records.");
            }
        }
    }

    private void loadCourses() {
        ObservableList<Course> courses =
                FXCollections.observableArrayList(
                        courseService.getAllCourses());

        courseTable.setItems(courses);
    }

    private void clearFields() {
        nameField.clear();
        codeField.clear();
        courseTable.getSelectionModel().clearSelection();
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(
                title.equals("Error")
                        ? Alert.AlertType.ERROR
                        : title.equals("Warning")
                                ? Alert.AlertType.WARNING
                                : Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}