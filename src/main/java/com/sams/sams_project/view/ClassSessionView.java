package com.sams.sams_project.view;

import com.sams.sams_project.model.ClassSession;
import com.sams.sams_project.model.Course;
import com.sams.sams_project.model.Subject;
import com.sams.sams_project.model.Lecturer;
import com.sams.sams_project.service.ClassSessionService;
import com.sams.sams_project.service.CourseService;
import com.sams.sams_project.service.SubjectService;
import com.sams.sams_project.service.LecturerService;

import java.time.LocalDate;
import java.time.LocalTime;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

public class ClassSessionView extends VBox {

    private final ClassSessionService sessionService =
            new ClassSessionService();

    private final CourseService courseService =
            new CourseService();

    private final SubjectService subjectService =
            new SubjectService();

    private final LecturerService lecturerService =
            new LecturerService();

    private final ComboBox<Course> courseBox = new ComboBox<>();
    private final ComboBox<Subject> subjectBox = new ComboBox<>();
    private final ComboBox<Lecturer> lecturerBox = new ComboBox<>();

    private final DatePicker datePicker = new DatePicker();

    private final TextField startTimeField = new TextField();
    private final TextField endTimeField = new TextField();

    private final TableView<ClassSession> sessionTable =
            new TableView<>();

    public ClassSessionView() {

        setSpacing(12);
        setPadding(new Insets(15));

        Label title = new Label("Class Scheduling");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        courseBox.setPromptText("Select course");
        subjectBox.setPromptText("Select subject");
        lecturerBox.setPromptText("Select lecturer");

        courseBox.setItems(FXCollections.observableArrayList(
                courseService.getAllCourses()));

        subjectBox.setItems(FXCollections.observableArrayList(
                subjectService.getAllSubjects()));

        lecturerBox.setItems(FXCollections.observableArrayList(
                lecturerService.getAllLecturers()));

        courseBox.setConverter(new javafx.util.StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                return course == null ? "" : course.getCourseName();
            }

            @Override
            public Course fromString(String value) {
                return null;
            }
        });

        subjectBox.setConverter(new javafx.util.StringConverter<Subject>() {
            @Override
            public String toString(Subject subject) {
                return subject == null ? ""
                        : subject.getSubjectName()
                        + " (" + subject.getSubjectCode() + ")";
            }

            @Override
            public Subject fromString(String value) {
                return null;
            }
        });

        lecturerBox.setConverter(
                new javafx.util.StringConverter<Lecturer>() {
            @Override
            public String toString(Lecturer lecturer) {
                return lecturer == null ? ""
                        : lecturer.getLecturerName();
            }

            @Override
            public Lecturer fromString(String value) {
                return null;
            }
        });

        datePicker.setPromptText("Select class date");

        startTimeField.setPromptText("Start time (HH:mm)");
        endTimeField.setPromptText("End time (HH:mm)");

        Button addButton = new Button("Schedule Class");
        Button updateButton = new Button("Update Selected");
        Button deleteButton = new Button("Delete Selected");
        Button refreshButton = new Button("Refresh");

        addButton.setOnAction(e -> addSession());
        updateButton.setOnAction(e -> updateSession());
        deleteButton.setOnAction(e -> deleteSession());
        refreshButton.setOnAction(e -> loadSessions());

        TableColumn<ClassSession, Number> idColumn =
                new TableColumn<>("Session ID");

        idColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getSessionId()));

        TableColumn<ClassSession, String> dateColumn =
                new TableColumn<>("Date");

        dateColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getClassDate().toString()));

        TableColumn<ClassSession, String> startColumn =
                new TableColumn<>("Start");

        startColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getStartTime().toString()));

        TableColumn<ClassSession, String> endColumn =
                new TableColumn<>("End");

        endColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getEndTime().toString()));

        sessionTable.getColumns().addAll(
                idColumn, dateColumn, startColumn, endColumn);

        sessionTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY);

        sessionTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldValue, selected) -> {
                    if (selected != null) {
                        populateFields(selected);
                    }
                });

        HBox firstRow = new HBox(10, courseBox, subjectBox);
        HBox secondRow = new HBox(10, lecturerBox, datePicker);
        HBox thirdRow = new HBox(
                10, startTimeField, endTimeField);

        HBox buttonRow = new HBox(
                10, addButton, updateButton,
                deleteButton, refreshButton);

        getChildren().addAll(
                title, firstRow, secondRow,
                thirdRow, buttonRow, sessionTable);

        loadSessions();
    }

    private void addSession() {
        try {
            ClassSession session = readForm();

            if (sessionService.addSession(session)) {
                showMessage("Success", "Class scheduled successfully.");
                clearFields();
                loadSessions();
            } else {
                showMessage("Error",
                        "Could not schedule class. Check your selections "
                        + "and database relationships.");
            }

        } catch (Exception e) {
            showMessage("Invalid Input",
                    "Check the selected course, subject, lecturer, date, "
                    + "and times. Use HH:mm for times.");
        }
    }

    private void updateSession() {
        ClassSession selected = sessionTable.getSelectionModel()
                .getSelectedItem();

        if (selected == null) {
            showMessage("Warning", "Select a class session first.");
            return;
        }

        try {
            ClassSession updated = readForm();
            updated.setSessionId(selected.getSessionId());

            if (sessionService.updateSession(updated)) {
                showMessage("Success", "Class updated successfully.");
                clearFields();
                loadSessions();
            } else {
                showMessage("Error",
                        "Could not update class. Check the selected values.");
            }

        } catch (Exception e) {
            showMessage("Invalid Input",
                    "Check the date and time fields. Use HH:mm for times.");
        }
    }

    private void deleteSession() {
        ClassSession selected = sessionTable.getSelectionModel()
                .getSelectedItem();

        if (selected == null) {
            showMessage("Warning", "Select a class session first.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete selected class session?");
        confirmation.setContentText(
                "Attendance records may prevent deletion.");

        java.util.Optional<ButtonType> result =
                confirmation.showAndWait();

        if (result.isPresent()
                && result.get() == ButtonType.OK) {

            if (sessionService.deleteSession(
                    selected.getSessionId())) {

                showMessage("Success", "Class deleted successfully.");
                clearFields();
                loadSessions();

            } else {
                showMessage("Error",
                        "Could not delete class. Check whether attendance "
                        + "records are linked to this session.");
            }
        }
    }

    private ClassSession readForm() {
        Course course = courseBox.getValue();
        Subject subject = subjectBox.getValue();
        Lecturer lecturer = lecturerBox.getValue();

        if (course == null || subject == null || lecturer == null
                || datePicker.getValue() == null) {
            throw new IllegalArgumentException("Missing selection");
        }

        LocalTime start = LocalTime.parse(startTimeField.getText().trim());
        LocalTime end = LocalTime.parse(endTimeField.getText().trim());

        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("Invalid time range");
        }

        ClassSession session = new ClassSession();

        session.setCourseId(course.getCourseId());
        session.setSubjectId(subject.getSubjectId());
        session.setLecturerId(lecturer.getLecturerId());
        session.setClassDate(datePicker.getValue());
        session.setStartTime(start);
        session.setEndTime(end);

        return session;
    }

    private void populateFields(ClassSession session) {
        datePicker.setValue(session.getClassDate());
        startTimeField.setText(session.getStartTime().toString());
        endTimeField.setText(session.getEndTime().toString());

        courseBox.getItems().stream()
                .filter(c -> c.getCourseId() == session.getCourseId())
                .findFirst().ifPresent(courseBox::setValue);

        subjectBox.getItems().stream()
                .filter(s -> s.getSubjectId() == session.getSubjectId())
                .findFirst().ifPresent(subjectBox::setValue);

        lecturerBox.getItems().stream()
                .filter(l -> l.getLecturerId() == session.getLecturerId())
                .findFirst().ifPresent(lecturerBox::setValue);
    }

    private void loadSessions() {
        sessionTable.setItems(
                FXCollections.observableArrayList(
                        sessionService.getAllSessions()));
    }

    private void clearFields() {
        courseBox.setValue(null);
        subjectBox.setValue(null);
        lecturerBox.setValue(null);
        datePicker.setValue(null);
        startTimeField.clear();
        endTimeField.clear();
        sessionTable.getSelectionModel().clearSelection();
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