package edu.ucsy.app.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Home {

    @FXML private TextField pollUrlField;
    @FXML private TextField pollCodeField;
    @FXML private Label activePollsLabel;
    @FXML private Label totalVotesLabel;
    @FXML private Label completedPollsLabel;

    @FXML
    public void initialize() {
        loadStatistics();
        setupFieldLogic();
    }

    private void setupFieldLogic() {
        pollUrlField.textProperty().addListener((a,b,c) -> {
            if (!c.isEmpty()) pollCodeField.clear();
        });

        pollCodeField.textProperty().addListener((a,b,c) -> {
            if (!c.isEmpty()) pollUrlField.clear();
        });
    }

    private void loadStatistics() {
        activePollsLabel.setText("0");
        totalVotesLabel.setText("0");
        completedPollsLabel.setText("0");
    }

    @FXML
    private void handleJoinPoll() {
        if (pollUrlField.getText().isBlank() && pollCodeField.getText().isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Please enter a poll URL or code");
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