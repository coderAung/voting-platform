package edu.ucsy.app;

import edu.ucsy.app.ui.controller.MasterLayout;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

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

        FXMLLoader loader = new FXMLLoader(MasterLayout.class.getResource("MasterLayout.fxml"));

        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();

        Scene scene = new Scene(root, 1024, 768);
        stage.setScene(scene);
        stage.setTitle("Voting Platform");
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
