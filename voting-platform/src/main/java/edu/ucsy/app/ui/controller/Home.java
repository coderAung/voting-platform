package edu.ucsy.app.ui.controller;

import edu.ucsy.app.ui.Page;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Home {

    private final MasterLayout masterLayout;

    @FXML private TextField newPollTitle;
    @FXML private TextField pollCodeField;
    @FXML private VBox choicesContainer;

    @FXML
    public void initialize() {
        setupFieldLogic();
    }

    private void setupFieldLogic() {
        pollCodeField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isBlank()) newPollTitle.clear();
        });
    }

    @FXML
    private void handleCreatePoll() {
        masterLayout.showPage(ActivePoll.class, Page.ActivePoll);
    }

    @FXML
    private void handleViewResults() {
        masterLayout.showPage(ActivePoll.class, Page.ActivePoll);
    }

    @FXML
    private void handleViewHistory() {
        masterLayout.showPage(History.class, Page.History);
    }

    @FXML
    private void handleJoinPoll() {
        if (pollCodeField.getText().isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Please enter a server IP or poll code");
            return;
        }
        showAlert(Alert.AlertType.INFORMATION, "Join Poll feature coming soon!");
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}