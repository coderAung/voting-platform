package edu.ucsy.app.ui.controller;

import edu.ucsy.app.rmi.dto.PollForm;
import edu.ucsy.app.server.service.PollManagementService;
import edu.ucsy.app.ui.Page;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Home {

    private final MasterLayout masterLayout;
    private final PollManagementService pollService;

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
    private void handleAddOption(ActionEvent event){
        int count = choicesContainer.getChildren().size()+1;
        TextField newOption = new TextField();
        newOption.setPromptText("Option "+ count);
        choicesContainer.getChildren().add(newOption);
    }
    @FXML
    private void handleCreatePoll() {
        if (newPollTitle.getText().isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Please enter a poll question.");
            return;
        }

        List<String> options = choicesContainer.getChildren().stream()
                .filter(n -> n instanceof TextField)
                .map(n -> ((TextField) n).getText())
                .filter(t -> !t.isBlank())
                .collect(Collectors.toList());

        if (options.size() < 2) {
            showAlert(Alert.AlertType.WARNING, "Please enter at least 2 options.");
            return;
        }

        var form = new PollForm(
                newPollTitle.getText(),
                LocalDateTime.now().plusHours(1),
                null,
                options
        );
        UUID pollId = pollService.create(form);

        masterLayout.showPage(ActivePoll.class, Page.ActivePoll);
        var activePoll = masterLayout.getController(ActivePoll.class);
        activePoll.loadPoll(pollId);
    }

    @FXML
    private void handleViewHistory() {
        masterLayout.showPage(History.class, Page.History);
    }

    @FXML
    private void handleJoinPoll() {
        if (pollCodeField.getText().isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Please enter a server IP or poll code.");
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