package com.sams.sams_project.view;

import com.sams.sams_project.model.Attendance;
import com.sams.sams_project.model.ClassSession;
import com.sams.sams_project.model.Student;
import com.sams.sams_project.service.AttendanceService;
import com.sams.sams_project.service.ClassSessionService;
import com.sams.sams_project.service.StudentService;

import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.util.StringConverter;

public class AttendanceView extends VBox {

    private final AttendanceService attendanceService =
            new AttendanceService();

    private final ClassSessionService sessionService =
            new ClassSessionService();

    private final StudentService studentService =
            new StudentService();

    private final ComboBox<ClassSession> sessionBox =
            new ComboBox<>();

    private final ComboBox<Student> studentBox =
            new ComboBox<>();

    private final ComboBox<String> statusBox =
            new ComboBox<>();

    private final TableView<Attendance> attendanceTable =
            new TableView<>();

    public AttendanceView() {
        setSpacing(12);
        setPadding(new Insets(15));

        Label title = new Label("Attendance Management");
        title.setStyle(
                "-fx-font-size: 22px; -fx-font-weight: bold;");

        sessionBox.setPromptText("Select class session");
        studentBox.setPromptText("Select student");
        statusBox.setPromptText("Select status");

        sessionBox.setMaxWidth(Double.MAX_VALUE);
        studentBox.setMaxWidth(Double.MAX_VALUE);
        statusBox.setMaxWidth(Double.MAX_VALUE);

        statusBox.setItems(FXCollections.observableArrayList(
                "Present", "Absent", "Late"));

        sessionBox.setConverter(new StringConverter<ClassSession>() {
            @Override
            public String toString(ClassSession session) {
                if (session == null) {
                    return "";
                }

                return "Session " + session.getSessionId()
                        + " - " + session.getClassDate()
                        + " (" + session.getStartTime()
                        + " - " + session.getEndTime() + ")";
            }

            @Override
            public ClassSession fromString(String value) {
                return null;
            }
        });

        studentBox.setConverter(new StringConverter<Student>() {
            @Override
            public String toString(Student student) {
                if (student == null) {
                    return "";
                }

                return student.getRegistrationNo()
                        + " - " + student.getStudentName();
            }

            @Override
            public Student fromString(String value) {
                return null;
            }
        });

        Button addButton = new Button("Record Attendance");
        Button updateButton = new Button("Update Selected");
        Button deleteButton = new Button("Delete Selected");
        Button refreshButton = new Button("Refresh");

        addButton.setOnAction(e -> addAttendance());
        updateButton.setOnAction(e -> updateAttendance());
        deleteButton.setOnAction(e -> deleteAttendance());
        refreshButton.setOnAction(e -> {
            loadOptions();
            loadAttendance();
        });

        TableColumn<Attendance, Number> idColumn =
                new TableColumn<>("Attendance ID");

        idColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getAttendanceId()));

        TableColumn<Attendance, String> sessionColumn =
                new TableColumn<>("Class Date & Time");

        sessionColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        getSessionDetails(
                                data.getValue().getSessionId())));

        TableColumn<Attendance, String> studentColumn =
                new TableColumn<>("Student Name");

        studentColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        getStudentName(
                                data.getValue().getStudentId())));

        TableColumn<Attendance, String> statusColumn =
                new TableColumn<>("Status");

        statusColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getStatus()));

        attendanceTable.getColumns().addAll(
                idColumn, sessionColumn,
                studentColumn, statusColumn);

        attendanceTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY);

        attendanceTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldValue, selected) -> {
                    if (selected != null) {
                        populateFields(selected);
                    }
                });

        HBox firstRow = new HBox(10, sessionBox, studentBox);
        HBox secondRow = new HBox(10, statusBox);

        firstRow.setFillHeight(true);

        HBox.setHgrow(sessionBox, Priority.ALWAYS);
        HBox.setHgrow(studentBox, Priority.ALWAYS);
        HBox.setHgrow(statusBox, Priority.ALWAYS);

        HBox buttonRow = new HBox(
                10, addButton, updateButton,
                deleteButton, refreshButton);

        getChildren().addAll(
                title, firstRow, secondRow,
                buttonRow, attendanceTable);

        loadOptions();
        loadAttendance();
    }

    private void loadOptions() {
        List<ClassSession> sessions =
                sessionService.getAllSessions();

        sessionBox.setItems(
                FXCollections.observableArrayList(sessions));

        List<Student> students =
                studentService.getAllStudents();

        studentBox.setItems(
                FXCollections.observableArrayList(students));
    }

    private String getStudentName(int studentId) {
        return studentBox.getItems().stream()
                .filter(student ->
                        student.getStudentId() == studentId)
                .map(Student::getStudentName)
                .findFirst()
                .orElse("Unknown Student (ID: "
                        + studentId + ")");
    }

    private String getSessionDetails(int sessionId) {
        return sessionBox.getItems().stream()
                .filter(session ->
                        session.getSessionId() == sessionId)
                .map(session -> session.getClassDate()
                        + " (" + session.getStartTime()
                        + " - " + session.getEndTime() + ")")
                .findFirst()
                .orElse("Unknown Session (ID: "
                        + sessionId + ")");
    }

    private Attendance readForm() {
        ClassSession session = sessionBox.getValue();
        Student student = studentBox.getValue();
        String status = statusBox.getValue();

        if (session == null || student == null || status == null) {
            throw new IllegalArgumentException(
                    "Select a session, student, and status.");
        }

        Attendance attendance = new Attendance();

        attendance.setSessionId(session.getSessionId());
        attendance.setStudentId(student.getStudentId());
        attendance.setStatus(status);

        return attendance;
    }

    private void addAttendance() {
        try {
            Attendance attendance = readForm();

            if (attendanceService.addAttendance(attendance)) {
                showMessage("Success",
                        "Attendance recorded successfully.");
                clearFields();
                loadAttendance();
            } else {
                showMessage("Error",
                        "Could not record attendance. Check the selections "
                        + "and database relationships.");
            }
        } catch (IllegalArgumentException e) {
            showMessage("Warning", e.getMessage());
        }
    }

    private void updateAttendance() {
        Attendance selected = attendanceTable.getSelectionModel()
                .getSelectedItem();

        if (selected == null) {
            showMessage("Warning",
                    "Select an attendance record first.");
            return;
        }

        try {
            Attendance updated = readForm();
            updated.setAttendanceId(selected.getAttendanceId());

            if (attendanceService.updateAttendance(updated)) {
                showMessage("Success",
                        "Attendance updated successfully.");
                clearFields();
                loadAttendance();
            } else {
                showMessage("Error",
                        "Could not update attendance.");
            }
        } catch (IllegalArgumentException e) {
            showMessage("Warning", e.getMessage());
        }
    }

    private void deleteAttendance() {
        Attendance selected = attendanceTable.getSelectionModel()
                .getSelectedItem();

        if (selected == null) {
            showMessage("Warning",
                    "Select an attendance record first.");
            return;
        }

        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION);

        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete attendance record?");
        confirmation.setContentText(
                "This action cannot be undone.");

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent()
                && result.get() == ButtonType.OK) {

            if (attendanceService.deleteAttendance(
                    selected.getAttendanceId())) {
                showMessage("Success",
                        "Attendance deleted successfully.");
                clearFields();
                loadAttendance();
            } else {
                showMessage("Error",
                        "Could not delete attendance record.");
            }
        }
    }

    private void populateFields(Attendance attendance) {
        sessionBox.getItems().stream()
                .filter(session ->
                        session.getSessionId()
                        == attendance.getSessionId())
                .findFirst()
                .ifPresent(sessionBox::setValue);

        studentBox.getItems().stream()
                .filter(student ->
                        student.getStudentId()
                        == attendance.getStudentId())
                .findFirst()
                .ifPresent(studentBox::setValue);

        statusBox.setValue(attendance.getStatus());
    }

    private void loadAttendance() {
        attendanceTable.setItems(
                FXCollections.observableArrayList(
                        attendanceService.getAllAttendance()));
    }

    private void clearFields() {
        sessionBox.setValue(null);
        studentBox.setValue(null);
        statusBox.setValue(null);
        attendanceTable.getSelectionModel().clearSelection();
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