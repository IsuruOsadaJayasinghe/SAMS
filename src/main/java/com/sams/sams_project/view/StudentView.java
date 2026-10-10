package com.sams.sams_project.view;

import com.sams.sams_project.model.Course;
import com.sams.sams_project.model.Student;
import com.sams.sams_project.service.CourseService;
import com.sams.sams_project.service.StudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class StudentView extends VBox {

    private final StudentService studentService = new StudentService();
    private final CourseService courseService = new CourseService();

    private final TableView<Student> studentTable = new TableView<>();

    private final TextField registrationField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField emailField = new TextField();
    private final TextField phoneField = new TextField();

    private final ComboBox<Course> courseComboBox = new ComboBox<>();

    public StudentView() {
        setSpacing(10);
        setPadding(new Insets(15));

        Label title = new Label("Student Management");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        registrationField.setPromptText("Registration number");
        nameField.setPromptText("Student name");
        emailField.setPromptText("Email");
        phoneField.setPromptText("Phone number");
        courseComboBox.setPromptText("Select course");

        TableColumn<Student, Integer> idColumn =
                new TableColumn<>("ID");
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("studentId"));

        TableColumn<Student, String> registrationColumn =
                new TableColumn<>("Registration No.");
        registrationColumn.setCellValueFactory(
                new PropertyValueFactory<>("registrationNo"));

        TableColumn<Student, String> nameColumn =
                new TableColumn<>("Student Name");
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("studentName"));

        TableColumn<Student, String> emailColumn =
                new TableColumn<>("Email");
        emailColumn.setCellValueFactory(
                new PropertyValueFactory<>("email"));

        TableColumn<Student, String> phoneColumn =
                new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(
                new PropertyValueFactory<>("phone"));

        TableColumn<Student, Integer> courseColumn =
                new TableColumn<>("Course ID");
        courseColumn.setCellValueFactory(
                new PropertyValueFactory<>("courseId"));

        studentTable.getColumns().addAll(
                idColumn, registrationColumn, nameColumn,
                emailColumn, phoneColumn, courseColumn);

        studentTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY);

        studentTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldStudent, selectedStudent) -> {
                    if (selectedStudent != null) {
                        registrationField.setText(
                                selectedStudent.getRegistrationNo());
                        nameField.setText(
                                selectedStudent.getStudentName());
                        emailField.setText(
                                selectedStudent.getEmail());
                        phoneField.setText(
                                selectedStudent.getPhone());

                        for (Course course : courseComboBox.getItems()) {
                            if (course.getCourseId()
                                    == selectedStudent.getCourseId()) {
                                courseComboBox.setValue(course);
                                break;
                            }
                        }
                    }
                });

        Button addButton = new Button("Add Student");
        Button updateButton = new Button("Update Student");
        Button deleteButton = new Button("Delete Student");
        Button clearButton = new Button("Clear");
        Button refreshButton = new Button("Refresh");

        addButton.setOnAction(e -> addStudent());
        updateButton.setOnAction(e -> updateStudent());
        deleteButton.setOnAction(e -> deleteStudent());
        clearButton.setOnAction(e -> clearFields());
        refreshButton.setOnAction(e -> loadStudents());

        HBox firstRow = new HBox(
                10, registrationField, nameField);

        HBox secondRow = new HBox(
                10, emailField, phoneField, courseComboBox);

        HBox buttons = new HBox(
                10, addButton, updateButton,
                deleteButton, clearButton, refreshButton);

        getChildren().addAll(
                title, firstRow, secondRow, buttons, studentTable);

        loadCourses();
        loadStudents();
    }

    private void loadCourses() {
        List<Course> courses = courseService.getAllCourses();

        courseComboBox.setItems(
                FXCollections.observableArrayList(courses));

        courseComboBox.setConverter(
                new javafx.util.StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                return course == null ? ""
                        : course.getCourseName()
                        + " (" + course.getCourseCode() + ")";
            }

            @Override
            public Course fromString(String value) {
                return null;
            }
        });
    }

    private void addStudent() {
        Course course = courseComboBox.getValue();

        if (course == null) {
            showMessage("Warning", "Please select a course.");
            return;
        }

        Student student = new Student();
        student.setRegistrationNo(registrationField.getText());
        student.setStudentName(nameField.getText());
        student.setEmail(emailField.getText());
        student.setPhone(phoneField.getText());
        student.setCourseId(course.getCourseId());

        if (studentService.addStudent(student)) {
            showMessage("Success", "Student added successfully.");
            clearFields();
            loadStudents();
        } else {
            showMessage("Error",
                    "Student could not be added. Check the details and registration number.");
        }
    }

    private void updateStudent() {
        Student selected = studentTable.getSelectionModel()
                .getSelectedItem();

        Course course = courseComboBox.getValue();

        if (selected == null) {
            showMessage("Warning", "Please select a student first.");
            return;
        }

        if (course == null) {
            showMessage("Warning", "Please select a course.");
            return;
        }

        selected.setRegistrationNo(registrationField.getText());
        selected.setStudentName(nameField.getText());
        selected.setEmail(emailField.getText());
        selected.setPhone(phoneField.getText());
        selected.setCourseId(course.getCourseId());

        if (studentService.updateStudent(selected)) {
            showMessage("Success", "Student updated successfully.");
            clearFields();
            loadStudents();
        } else {
            showMessage("Error",
                    "Student could not be updated. Check the entered details.");
        }
    }

    private void deleteStudent() {
        Student selected = studentTable.getSelectionModel()
                .getSelectedItem();

        if (selected == null) {
            showMessage("Warning", "Please select a student first.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Delete");
        confirmation.setHeaderText(
                "Delete " + selected.getStudentName() + "?");
        confirmation.setContentText(
                "Students linked to attendance records may not be deletable.");

        java.util.Optional<javafx.scene.control.ButtonType> result =
                confirmation.showAndWait();

        if (result.isPresent()
                && result.get() == javafx.scene.control.ButtonType.OK) {
            if (studentService.deleteStudent(selected.getStudentId())) {
                showMessage("Success", "Student deleted successfully.");
                clearFields();
                loadStudents();
            } else {
                showMessage("Error",
                        "Student could not be deleted because they may have attendance records.");
            }
        }
    }

    private void loadStudents() {
        ObservableList<Student> students =
                FXCollections.observableArrayList(
                        studentService.getAllStudents());

        studentTable.setItems(students);
    }

    private void clearFields() {
        registrationField.clear();
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        courseComboBox.getSelectionModel().clearSelection();
        studentTable.getSelectionModel().clearSelection();
    }

    private void showMessage(String title, String message) {
        Alert.AlertType type = title.equals("Error")
                ? Alert.AlertType.ERROR
                : title.equals("Warning")
                        ? Alert.AlertType.WARNING
                        : Alert.AlertType.INFORMATION;

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}