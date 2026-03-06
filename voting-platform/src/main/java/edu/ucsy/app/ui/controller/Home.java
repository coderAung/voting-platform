package edu.ucsy.app.ui.controller;

import edu.ucsy.app.rmi.dto.PollForm;
import edu.ucsy.app.rmi.dto.output.PollInfo;
import edu.ucsy.app.server.service.PollManagementService;
import edu.ucsy.app.ui.Page;
import edu.ucsy.app.utils.RmiUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.net.UnknownHostException;
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
    @FXML private TextField voteLimit;
    @FXML private ComboBox<Integer> endTime;
    @FXML private TextField pollCodeField;
    @FXML private VBox choicesContainer;
    @FXML private Button createBtn;
    @FXML private Button activePoll;

    @FXML
    public void initialize() {

        createBtn.setDisable(MasterLayout.getCurrentPoll() != null);
        activePoll.setDisable(MasterLayout.getCurrentPoll() == null);

        activePoll.setOnAction(ev -> masterLayout.showPage(PollState.class, Page.PollState));

        setupFieldLogic();
        voteLimit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                voteLimit.setText(newValue.replaceAll("\\D", ""));
            }
        });

        endTime.getItems().addAll(10, 30, 60);

        choicesContainer.getChildren().clear();

        addOptionRow();
        addOptionRow();
    }

    private void setupFieldLogic() {
        pollCodeField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isBlank()) newPollTitle.clear();
        });
    }

    public void addOptionRow() {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        TextField newOption = new TextField();
        newOption.setPromptText("Option " + (choicesContainer.getChildren().size() + 1));
        HBox.setHgrow(newOption, Priority.ALWAYS);

        Button removeBtn = new Button("✕");
        removeBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold;");

        removeBtn.setOnAction(e -> choicesContainer.getChildren().remove(row));

        row.getChildren().addAll(newOption, removeBtn);
        choicesContainer.getChildren().add(row);
    }

    @FXML
    public void handleAddOption(ActionEvent event) {
        addOptionRow();
    }
    @FXML
    public void handleCreatePoll() {
        if (newPollTitle.getText().isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Please enter a poll question.");
            return;
        }

        List<String> options = choicesContainer.getChildren().stream()
                .map(node -> {
                    if (node instanceof HBox hb) {
                        return hb.getChildren().stream()
                                .filter(child -> child instanceof TextField)
                                .map(child -> ((TextField) child).getText())
                                .findFirst().orElse("");
                    }
                    if (node instanceof TextField tf) {
                        return tf.getText();
                    }
                    return "";
                })
                .map(String::trim)
                .filter(t -> !t.isEmpty())
                .collect(Collectors.toList());

        if (options.size() < 2) {
            showAlert(Alert.AlertType.WARNING, "Please enter at least 2 options.");
            return;
        }

        try {
            var form = new PollForm(
                    newPollTitle.getText(),
                    RmiUtils.getLocalIpAddress(),
                    endTime.getValue() == null ? null : LocalDateTime.now().plusHours(endTime.getValue()),
                    StringUtils.hasLength(voteLimit.getText()) || voteLimit.getText().equals("") ? null : Integer.parseInt(voteLimit.getText()),
                    options
            );
            UUID pollId = pollService.create(form);
            MasterLayout.setCurrentPoll(new PollInfo(pollService.findById(pollId)));
            masterLayout.showPage(PollState.class, Page.PollState);

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
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