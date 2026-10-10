package com.sams.sams_project.view;

import com.sams.sams_project.model.Lecturer;
import com.sams.sams_project.service.LecturerService;
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

public class LecturerView extends VBox {

    private final LecturerService lecturerService = new LecturerService();

    private final TableView<Lecturer> lecturerTable = new TableView<>();

    private final TextField nameField = new TextField();
    private final TextField emailField = new TextField();
    private final TextField phoneField = new TextField();

    public LecturerView() {
        setSpacing(10);
        setPadding(new Insets(15));

        Label title = new Label("Lecturer Management");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        nameField.setPromptText("Lecturer name");
        emailField.setPromptText("Email");
        phoneField.setPromptText("Phone number");

        TableColumn<Lecturer, Integer> idColumn =
                new TableColumn<>("ID");
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("lecturerId"));

        TableColumn<Lecturer, String> nameColumn =
                new TableColumn<>("Lecturer Name");
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("lecturerName"));

        TableColumn<Lecturer, String> emailColumn =
                new TableColumn<>("Email");
        emailColumn.setCellValueFactory(
                new PropertyValueFactory<>("email"));

        TableColumn<Lecturer, String> phoneColumn =
                new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(
                new PropertyValueFactory<>("phone"));

        lecturerTable.getColumns().addAll(
                idColumn, nameColumn, emailColumn, phoneColumn);

        lecturerTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY);

        lecturerTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldLecturer, selectedLecturer) -> {
                    if (selectedLecturer != null) {
                        nameField.setText(selectedLecturer.getLecturerName());
                        emailField.setText(selectedLecturer.getEmail());
                        phoneField.setText(selectedLecturer.getPhone());
                    }
                });

        Button addButton = new Button("Add Lecturer");
        Button updateButton = new Button("Update Lecturer");
        Button deleteButton = new Button("Delete Lecturer");
        Button clearButton = new Button("Clear");
        Button refreshButton = new Button("Refresh");

        addButton.setOnAction(e -> addLecturer());
        updateButton.setOnAction(e -> updateLecturer());
        deleteButton.setOnAction(e -> deleteLecturer());
        clearButton.setOnAction(e -> clearFields());
        refreshButton.setOnAction(e -> loadLecturers());

        HBox fields = new HBox(
                10, nameField, emailField, phoneField);

        HBox buttons = new HBox(
                10, addButton, updateButton,
                deleteButton, clearButton, refreshButton);

        getChildren().addAll(title, fields, buttons, lecturerTable);

        loadLecturers();
    }

    private void addLecturer() {
        Lecturer lecturer = new Lecturer();
        lecturer.setLecturerName(nameField.getText());
        lecturer.setEmail(emailField.getText());
        lecturer.setPhone(phoneField.getText());

        if (lecturerService.addLecturer(lecturer)) {
            showMessage("Success", "Lecturer added successfully.");
            clearFields();
            loadLecturers();
        } else {
            showMessage("Error",
                    "Lecturer could not be added. Check the entered details.");
        }
    }

    private void updateLecturer() {
        Lecturer selected = lecturerTable.getSelectionModel()
                .getSelectedItem();

        if (selected == null) {
            showMessage("Warning", "Please select a lecturer first.");
            return;
        }

        selected.setLecturerName(nameField.getText());
        selected.setEmail(emailField.getText());
        selected.setPhone(phoneField.getText());

        if (lecturerService.updateLecturer(selected)) {
            showMessage("Success", "Lecturer updated successfully.");
            clearFields();
            loadLecturers();
        } else {
            showMessage("Error",
                    "Lecturer could not be updated. Check the details.");
        }
    }

    private void deleteLecturer() {
        Lecturer selected = lecturerTable.getSelectionModel()
                .getSelectedItem();

        if (selected == null) {
            showMessage("Warning", "Please select a lecturer first.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Delete");
        confirmation.setHeaderText(
                "Delete " + selected.getLecturerName() + "?");
        confirmation.setContentText(
                "Lecturers linked to subjects or class sessions may not be deletable.");

        java.util.Optional<javafx.scene.control.ButtonType> result =
                confirmation.showAndWait();

        if (result.isPresent()
                && result.get() == javafx.scene.control.ButtonType.OK) {
            if (lecturerService.deleteLecturer(selected.getLecturerId())) {
                showMessage("Success", "Lecturer deleted successfully.");
                clearFields();
                loadLecturers();
            } else {
                showMessage("Error",
                        "Lecturer could not be deleted because they may be linked to other records.");
            }
        }
    }

    private void loadLecturers() {
        ObservableList<Lecturer> lecturers =
                FXCollections.observableArrayList(
                        lecturerService.getAllLecturers());

        lecturerTable.setItems(lecturers);
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        lecturerTable.getSelectionModel().clearSelection();
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