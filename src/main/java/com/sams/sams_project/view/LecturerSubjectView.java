package com.sams.sams_project.view;

import com.sams.sams_project.model.Lecturer;
import com.sams.sams_project.model.Subject;
import com.sams.sams_project.service.LecturerService;
import com.sams.sams_project.service.LecturerSubjectService;
import com.sams.sams_project.service.SubjectService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LecturerSubjectView extends VBox {

    private final LecturerService lecturerService = new LecturerService();
    private final SubjectService subjectService = new SubjectService();
    private final LecturerSubjectService assignmentService =
            new LecturerSubjectService();

    private final ComboBox<Lecturer> lecturerComboBox = new ComboBox<>();
    private final ComboBox<Subject> subjectComboBox = new ComboBox<>();
    private final ListView<String> assignmentList = new ListView<>();

    public LecturerSubjectView() {
        setSpacing(12);
        setPadding(new Insets(15));

        Label title = new Label("Lecturer Subject Assignments");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        lecturerComboBox.setPromptText("Select lecturer");
        subjectComboBox.setPromptText("Select subject");

        lecturerComboBox.setItems(
                FXCollections.observableArrayList(
                        lecturerService.getAllLecturers()));

        subjectComboBox.setItems(
                FXCollections.observableArrayList(
                        subjectService.getAllSubjects()));

        lecturerComboBox.setConverter(
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

        subjectComboBox.setConverter(
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

        Button assignButton = new Button("Assign Subject");
        Button removeButton = new Button("Remove Assignment");
        Button refreshButton = new Button("Refresh Assignments");

        assignButton.setOnAction(e -> assignSubject());
        removeButton.setOnAction(e -> removeAssignment());
        refreshButton.setOnAction(e -> loadAssignments());

        HBox selectionRow = new HBox(
                10, lecturerComboBox, subjectComboBox);

        HBox buttonRow = new HBox(
                10, assignButton, removeButton, refreshButton);

        Label assignmentTitle = new Label("Current Assignments");

        getChildren().addAll(
                title, selectionRow, buttonRow,
                assignmentTitle, assignmentList);

        loadAssignments();
    }

    private void assignSubject() {
        Lecturer lecturer = lecturerComboBox.getValue();
        Subject subject = subjectComboBox.getValue();

        if (lecturer == null || subject == null) {
            showMessage("Warning",
                    "Please select both a lecturer and a subject.");
            return;
        }

        boolean success = assignmentService.assignSubject(
                lecturer.getLecturerId(), subject.getSubjectId());

        if (success) {
            showMessage("Success", "Subject assigned successfully.");
            loadAssignments();
        } else {
            showMessage("Error",
                    "Assignment failed. It may already exist in the database.");
        }
    }

    private void removeAssignment() {
        Lecturer lecturer = lecturerComboBox.getValue();
        Subject subject = subjectComboBox.getValue();

        if (lecturer == null || subject == null) {
            showMessage("Warning",
                    "Select the lecturer and subject to remove.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Removal");
        confirmation.setHeaderText("Remove this subject assignment?");
        confirmation.setContentText(
                "This removes the lecturer-subject link, not the subject itself.");

        java.util.Optional<javafx.scene.control.ButtonType> result =
                confirmation.showAndWait();

        if (result.isPresent()
                && result.get() == javafx.scene.control.ButtonType.OK) {

            boolean success = assignmentService.removeSubjectAssignment(
                    lecturer.getLecturerId(), subject.getSubjectId());

            if (success) {
                showMessage("Success", "Assignment removed successfully.");
                loadAssignments();
            } else {
                showMessage("Error",
                        "Assignment could not be removed. Check that it exists.");
            }
        }
    }

    private void loadAssignments() {
        assignmentList.setItems(
                FXCollections.observableArrayList(
                        assignmentService.getAssignments()));
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