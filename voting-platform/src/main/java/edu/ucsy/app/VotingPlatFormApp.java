package edu.ucsy.app;

import edu.ucsy.app.ui.controller.Home;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class VotingPlatFormApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        var view = (Parent) FXMLLoader.load(Objects.requireNonNull(Home.class.getResource("Home.fxml")));
        stage.setScene(new Scene(view));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
