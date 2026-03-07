package edu.ucsy.app;

import edu.ucsy.app.server.entities.Poll;
import edu.ucsy.app.server.service.PollManagementService;
import edu.ucsy.app.server.service.RmiService;
import edu.ucsy.app.ui.controller.MasterLayout;
import edu.ucsy.app.utils.RmiUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

@SpringBootApplication
public class VotingPlatFormApp extends Application {

    private static ConfigurableApplicationContext context;

    public static <T> T getBean(Class<T> clz) {
        if (context == null) {
            throw new RuntimeException("Spring context is null.");
        }
        return context.getBean(clz);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(MasterLayout.class.getResource("MasterLayout.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);

        var pollService = context.getBean(PollManagementService.class);

        stage.setOnCloseRequest(ev -> {
            if(MasterLayout.getCurrentPoll() != null) {
                pollService.changeStatus(MasterLayout.getCurrentPoll().id(), Poll.Status.Fail);
            }
        });
        stage.setTitle("Voting Platform");
        stage.show();
    }

    public static void main(String[] args) {
        context = SpringApplication.run(VotingPlatFormApp.class, args);  // pass args here
        launch(args);
    }

    @Override
    public void stop() {
        if (context != null) {
            if(MasterLayout.getCurrentPoll() != null) {
                try {
                    context.getBean(RmiService.class).cleanUp(RmiUtils.getLocalIpAddress());
                } catch (MalformedURLException | RemoteException | UnknownHostException | NotBoundException e) {
                    throw new RuntimeException(e);
                }
            }
            context.close();
        }
    }
}