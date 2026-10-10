package com.sams.sams_project;

import com.sams.sams_project.view.ClassSessionView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestClassSessionView extends Application {

    @Override
    public void start(Stage stage) {
        ClassSessionView view = new ClassSessionView();

        Scene scene = new Scene(view, 1000, 600);

        stage.setTitle("SAMS - Class Scheduling Test");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}