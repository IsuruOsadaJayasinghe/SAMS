package com.sams.sams_project.view;

import com.sams.sams_project.model.User;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LecturerDashboardView {

    private final User user;

    public LecturerDashboardView(User user) {
        this.user = user;
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("SAMS - Lecturer Dashboard");

        Label heading = new Label(
                "Lecturer Dashboard - " + user.getUsername()
        );
        heading.setStyle(
                "-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 15;"
        );

        TabPane tabs = new TabPane();

        addTab(tabs, "Class Sessions", new ClassSessionView());
        addTab(tabs, "Attendance", new AttendanceView());

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