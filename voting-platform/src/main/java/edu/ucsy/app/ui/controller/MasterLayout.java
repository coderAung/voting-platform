package edu.ucsy.app.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MasterLayout {

    @FXML private StackPane contentArea;

    // Constructor injection via Lombok
    private final ApplicationContext springContext;

    @FXML
    public void initialize() {
        showHome();
    }

    @FXML
    public void showHome() {
        loadView("/view/Home.fxml");
    }

    @FXML
    public void showActivePoll() {
        loadView("/view/ActivePoll.fxml");
    }

    @FXML
    public void showHistory() {
        loadView("/view/History.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            // Controller တွေကို Spring ထဲကနေပဲ ယူသုံးဖို့ သတ်မှတ်တာ
            loader.setControllerFactory(springContext::getBean);

            Parent view = loader.load();

            // အဟောင်းတွေကိုဖျက်ပြီး အသစ်ကို ထည့်မယ်
            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            System.err.println("Error loading: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
