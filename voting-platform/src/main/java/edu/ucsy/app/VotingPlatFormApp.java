package edu.ucsy.app;

import edu.ucsy.app.ui.controller.Home;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;

@SpringBootApplication
public class VotingPlatFormApp extends Application {

    private static ConfigurableApplicationContext context;

    public static <T> T getBean(Class<T> clz) {
        if(context == null) {
            throw new RuntimeException("Spring context is null.");
        }
        return context.getBean(clz);
    }

    @Override
    public void start(Stage stage) throws Exception {
        var view = (Parent) FXMLLoader.load(Objects.requireNonNull(Home.class.getResource("Home.fxml")));
        stage.setScene(new Scene(view));
        stage.show();
    }

    public static void main(String[] args) {
        context = SpringApplication.run(VotingPlatFormApp.class);
        launch(args);
    }

    @Override
    public void stop() {
        if(context != null) {
            context.close();
        }
    }
}
