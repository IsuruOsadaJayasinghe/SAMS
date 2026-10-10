package com.sams.sams_project.view;

import com.sams.sams_project.model.Course;
import com.sams.sams_project.model.Subject;
import com.sams.sams_project.service.CourseService;
import com.sams.sams_project.service.SubjectService;
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

public class SubjectView extends VBox {

    private final SubjectService subjectService = new SubjectService();
    private final CourseService courseService = new CourseService();

    private final TableView<Subject> subjectTable = new TableView<>();
    private final TextField nameField = new TextField();
    private final TextField codeField = new TextField();
    private final ComboBox<Course> courseComboBox = new ComboBox<>();

    public SubjectView() {
        setSpacing(10);
        setPadding(new Insets(15));

        Label title = new Label("Subject Management");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        nameField.setPromptText("Subject name");
        codeField.setPromptText("Subject code");
        courseComboBox.setPromptText("Select course");

        TableColumn<Subject, Integer> idColumn =
                new TableColumn<>("ID");
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("subjectId"));

        TableColumn<Subject, String> nameColumn =
                new TableColumn<>("Subject Name");
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("subjectName"));

        TableColumn<Subject, String> codeColumn =
                new TableColumn<>("Subject Code");
        codeColumn.setCellValueFactory(
                new PropertyValueFactory<>("subjectCode"));

        TableColumn<Subject, Integer> courseColumn =
                new TableColumn<>("Course ID");
        courseColumn.setCellValueFactory(
                new PropertyValueFactory<>("courseId"));

        subjectTable.getColumns().addAll(
                idColumn, nameColumn, codeColumn, courseColumn);

        subjectTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY);

        subjectTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldSubject, selectedSubject) -> {
                    if (selectedSubject != null) {
                        nameField.setText(selectedSubject.getSubjectName());
                        codeField.setText(selectedSubject.getSubjectCode());

                        for (Course course : courseComboBox.getItems()) {
                            if (course.getCourseId()
                                    == selectedSubject.getCourseId()) {
                                courseComboBox.setValue(course);
                                break;
                            }
                        }
                    }
                });

        Button addButton = new Button("Add Subject");
        Button updateButton = new Button("Update Subject");
        Button deleteButton = new Button("Delete Subject");
        Button clearButton = new Button("Clear");
        Button refreshButton = new Button("Refresh");

        addButton.setOnAction(e -> addSubject());
        updateButton.setOnAction(e -> updateSubject());
        deleteButton.setOnAction(e -> deleteSubject());
        clearButton.setOnAction(e -> clearFields());
        refreshButton.setOnAction(e -> loadSubjects());

        HBox fields = new HBox(
                10, nameField, codeField, courseComboBox);

        HBox buttons = new HBox(
                10, addButton, updateButton,
                deleteButton, clearButton, refreshButton);

        getChildren().addAll(title, fields, buttons, subjectTable);

        loadCourses();
        loadSubjects();
    }

    private void loadCourses() {
        List<Course> courses = courseService.getAllCourses();

        ObservableList<Course> courseItems =
                FXCollections.observableArrayList(courses);

        courseComboBox.setItems(courseItems);

        courseComboBox.setConverter(new javafx.util.StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                return course == null ? ""
                        : course.getCourseName() + " ("
                        + course.getCourseCode() + ")";
            }

            @Override
            public Course fromString(String string) {
                return null;
            }
        });
    }

    private void addSubject() {
        Course selectedCourse = courseComboBox.getValue();

        if (selectedCourse == null) {
            showMessage("Warning", "Please select a course.");
            return;
        }

        Subject subject = new Subject();
        subject.setSubjectName(nameField.getText());
        subject.setSubjectCode(codeField.getText());
        subject.setCourseId(selectedCourse.getCourseId());

        if (subjectService.addSubject(subject)) {
            showMessage("Success", "Subject added successfully.");
            clearFields();
            loadSubjects();
        } else {
            showMessage("Error",
                    "Subject could not be added. Check the details and subject code.");
        }
    }

    private void updateSubject() {
        Subject selected = subjectTable.getSelectionModel()
                .getSelectedItem();

        Course selectedCourse = courseComboBox.getValue();

        if (selected == null) {
            showMessage("Warning", "Please select a subject first.");
            return;
        }

        if (selectedCourse == null) {
            showMessage("Warning", "Please select a course.");
            return;
        }

        selected.setSubjectName(nameField.getText());
        selected.setSubjectCode(codeField.getText());
        selected.setCourseId(selectedCourse.getCourseId());

        if (subjectService.updateSubject(selected)) {
            showMessage("Success", "Subject updated successfully.");
            clearFields();
            loadSubjects();
        } else {
            showMessage("Error",
                    "Subject could not be updated. Check the details.");
        }
    }

    private void deleteSubject() {
        Subject selected = subjectTable.getSelectionModel()
                .getSelectedItem();

        if (selected == null) {
            showMessage("Warning", "Please select a subject first.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Delete");
        confirmation.setHeaderText("Delete " + selected.getSubjectName() + "?");
        confirmation.setContentText(
                "Subjects linked to class sessions or lecturer assignments may not be deletable.");

        java.util.Optional<javafx.scene.control.ButtonType> result =
                confirmation.showAndWait();

        if (result.isPresent()
                && result.get() == javafx.scene.control.ButtonType.OK) {
            if (subjectService.deleteSubject(selected.getSubjectId())) {
                showMessage("Success", "Subject deleted successfully.");
                clearFields();
                loadSubjects();
            } else {
                showMessage("Error",
                        "Subject could not be deleted because it may be linked to other records.");
            }
        }
    }

    private void loadSubjects() {
        ObservableList<Subject> subjects =
                FXCollections.observableArrayList(
                        subjectService.getAllSubjects());

        subjectTable.setItems(subjects);
    }

    private void clearFields() {
        nameField.clear();
        codeField.clear();
        courseComboBox.getSelectionModel().clearSelection();
        subjectTable.getSelectionModel().clearSelection();
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