package com.sams.sams_project.view;

import com.sams.sams_project.model.User;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AdminDashboardView {

    private final User user;

    public AdminDashboardView(User user) {
        this.user = user;
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("SAMS - Admin Dashboard");

        Label heading = new Label(
                "Administrator Dashboard - " + user.getUsername()
        );
        heading.setStyle(
                "-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 15;"
        );

        TabPane tabs = new TabPane();

        addTab(tabs, "Lecturers", new LecturerView());
        addTab(tabs, "Lecturer Subjects", new LecturerSubjectView());

        BorderPane root = new BorderPane();
        root.setTop(heading);
        root.setCenter(tabs);

        stage.setScene(new Scene(root, 950, 650));
        stage.show();
    }

    private void addTab(TabPane tabs, String title,
                        javafx.scene.Node content) {
        Tab tab = new Tab(title, content);
        tab.setClosable(false);
        tabs.getTabs().add(tab);
    }
}