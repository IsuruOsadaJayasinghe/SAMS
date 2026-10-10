package com.sams.sams_project.view;

import com.sams.sams_project.model.Student;
import com.sams.sams_project.model.Course;
import com.sams.sams_project.model.Subject;
import com.sams.sams_project.service.AttendanceService;
import com.sams.sams_project.service.StudentService;
import com.sams.sams_project.service.CourseService;
import com.sams.sams_project.service.SubjectService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

public class AttendanceReportView extends VBox {

    private final AttendanceService attendanceService =
            new AttendanceService();

    private final StudentService studentService =
            new StudentService();

    private final CourseService courseService =
            new CourseService();

    private final SubjectService subjectService =
            new SubjectService();

    private final ComboBox<Student> studentBox =
            new ComboBox<>();

    private final ComboBox<Course> courseBox =
            new ComboBox<>();

    private final ComboBox<Subject> subjectBox =
            new ComboBox<>();

    private final DatePicker startDatePicker =
            new DatePicker();

    private final DatePicker endDatePicker =
            new DatePicker();

    private final TableView<Map<String, Object>> reportTable =
            new TableView<>();

    public AttendanceReportView() {

        setSpacing(12);
        setPadding(new Insets(15));

        Label title = new Label("Attendance Reports");
        title.setStyle(
                "-fx-font-size: 22px; -fx-font-weight: bold;");

        studentBox.setPromptText("All students");
        courseBox.setPromptText("All courses");
        subjectBox.setPromptText("All subjects");

        startDatePicker.setPromptText("Start date");
        endDatePicker.setPromptText("End date");

        studentBox.setMaxWidth(Double.MAX_VALUE);
        courseBox.setMaxWidth(Double.MAX_VALUE);
        subjectBox.setMaxWidth(Double.MAX_VALUE);

        studentBox.setItems(
                FXCollections.observableArrayList(
                        studentService.getAllStudents()));

        courseBox.setItems(
                FXCollections.observableArrayList(
                        courseService.getAllCourses()));

        subjectBox.setItems(
                FXCollections.observableArrayList(
                        subjectService.getAllSubjects()));

        studentBox.setConverter(
                new javafx.util.StringConverter<Student>() {
            @Override
            public String toString(Student student) {
                return student == null ? ""
                        : student.getRegistrationNo()
                        + " - " + student.getStudentName();
            }

            @Override
            public Student fromString(String value) {
                return null;
            }
        });

        courseBox.setConverter(
                new javafx.util.StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                return course == null ? ""
                        : course.getCourseName();
            }

            @Override
            public Course fromString(String value) {
                return null;
            }
        });

        subjectBox.setConverter(
                new javafx.util.StringConverter<Subject>() {
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

        Button filterButton = new Button("Generate Report");
        Button resetButton = new Button("Reset Filters");

        filterButton.setOnAction(e -> generateReport());
        resetButton.setOnAction(e -> resetFilters());

        HBox firstRow = new HBox(
                10, studentBox, courseBox, subjectBox);

        HBox secondRow = new HBox(
                10, startDatePicker, endDatePicker,
                filterButton, resetButton);

        HBox.setHgrow(studentBox, Priority.ALWAYS);
        HBox.setHgrow(courseBox, Priority.ALWAYS);
        HBox.setHgrow(subjectBox, Priority.ALWAYS);

        addColumn("Attendance ID", "attendanceId");
        addColumn("Registration No", "registrationNo");
        addColumn("Student Name", "studentName");
        addColumn("Course", "courseName");
        addColumn("Subject", "subjectName");
        addColumn("Class Date", "classDate");
        addColumn("Start Time", "startTime");
        addColumn("End Time", "endTime");
        addColumn("Status", "status");

        reportTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY);

        getChildren().addAll(
                title, firstRow, secondRow, reportTable);

        generateReport();
    }

    private void addColumn(String title, String key) {

        TableColumn<Map<String, Object>, String> column =
                new TableColumn<>(title);

        column.setCellValueFactory(data -> {
            Object value = data.getValue().get(key);

            return new javafx.beans.property.SimpleStringProperty(
                    value == null ? "" : value.toString());
        });

        reportTable.getColumns().add(column);
    }

    private void generateReport() {

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null
                && startDate.isAfter(endDate)) {
            showMessage(
                    "Invalid Date Range",
                    "Start date must not be after end date.");
            return;
        }

        try {
            Student student = studentBox.getValue();
            Course course = courseBox.getValue();
            Subject subject = subjectBox.getValue();

            List<Map<String, Object>> results =
                    attendanceService.getAttendanceReport(
                            student == null
                                    ? null : student.getStudentId(),
                            course == null
                                    ? null : course.getCourseId(),
                            subject == null
                                    ? null : subject.getSubjectId(),
                            startDate,
                            endDate);

            reportTable.setItems(
                    FXCollections.observableArrayList(results));

            if (results.isEmpty()) {
                showMessage(
                        "No Records",
                        "No attendance records match these filters.");
            }

        } catch (Exception ex) {
            showMessage(
                    "Report Error",
                    "Could not generate the report. Check the "
                    + "database connection and report query.");
            ex.printStackTrace();
        }
    }

    private void resetFilters() {
        studentBox.setValue(null);
        courseBox.setValue(null);
        subjectBox.setValue(null);
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);

        generateReport();
    }

    private void showMessage(String title, String message) {

        Alert.AlertType type =
                title.equals("Report Error")
                ? Alert.AlertType.ERROR
                : title.equals("Invalid Date Range")
                ? Alert.AlertType.WARNING
                : Alert.AlertType.INFORMATION;

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}