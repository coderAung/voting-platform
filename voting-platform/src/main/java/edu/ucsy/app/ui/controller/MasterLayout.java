package edu.ucsy.app.ui.controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MasterLayout {

    @FXML private StackPane contentArea;
    @FXML private Button homeBtn;
    @FXML private Button activePollBtn;
    @FXML private Button historyBtn;

    private final ApplicationContext springContext;

    @FXML
    public void initialize() {
        showHome();
    }

    @FXML
    public void showHome() {
        setActiveButton(homeBtn);
        loadView("/edu/ucsy/app/ui/controller/Home.fxml");
    }

    @FXML
    public void showActivePoll() {
        setActiveButton(activePollBtn);
        loadView("/edu/ucsy/app/ui/controller/ActivePoll.fxml");
    }

    @FXML
    public void showHistory() {
        setActiveButton(historyBtn);
        loadView("/edu/ucsy/app/ui/controller/History.fxml");
    }

    private void loadView(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            loader.setControllerFactory(springContext::getBean);

            Parent view = loader.load();

            FadeTransition fadeOut = new FadeTransition(Duration.millis(150), contentArea);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            fadeOut.setOnFinished(e -> {
                contentArea.getChildren().setAll(view);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(200), view);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.play();
            });

            fadeOut.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setActiveButton(Button active) {
        homeBtn.getStyleClass().remove("active");
        activePollBtn.getStyleClass().remove("active");
        historyBtn.getStyleClass().remove("active");

        active.getStyleClass().add("active");
    }
}