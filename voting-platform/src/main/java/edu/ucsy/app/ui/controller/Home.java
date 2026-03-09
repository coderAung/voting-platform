package edu.ucsy.app.ui.controller;

import edu.ucsy.app.rmi.VotingService;
import edu.ucsy.app.rmi.dto.Host;
import edu.ucsy.app.rmi.dto.PollForm;
import edu.ucsy.app.rmi.dto.output.PollInfo;
import edu.ucsy.app.server.VotingServerImpl;
import edu.ucsy.app.server.service.PollManagementService;
import edu.ucsy.app.server.service.PollSchedulingService;
import edu.ucsy.app.server.service.RmiService;
import edu.ucsy.app.ui.Page;
import edu.ucsy.app.utils.RmiUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static edu.ucsy.app.ui.AlertUtils.showAlert;

@Component
@RequiredArgsConstructor
public class Home {

    private final RmiService rmiService;
    private final ActivePoll activePoll;
    private final MasterLayout masterLayout;
    private final VotingService votingService;
    private final PollManagementService pollService;
    private final PollSchedulingService pollSchedulingService;

    @FXML private TextField newPollTitle;
    @FXML private TextField voteLimit;
    @FXML private ComboBox<Integer> endTime;
    @FXML private TextField pollCodeField;
    @FXML private VBox choicesContainer;
    @FXML private Button createBtn;
    @FXML private Button activePollBtn;

    @FXML
    public void initialize() {

        createBtn.setDisable(MasterLayout.getCurrentPoll() != null);
        activePollBtn.setDisable(MasterLayout.getCurrentPoll() == null);

        activePollBtn.setOnAction(ev -> masterLayout.showPage(PollState.class, Page.PollState));

        setupFieldLogic();
        voteLimit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                voteLimit.setText(newValue.replaceAll("\\D", ""));
            }
        });

        endTime.getItems().addAll(1, 3, 5, 10, 30, 60);

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
    public void handleAddOption() {
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
            var ipAddress = RmiUtils.getLocalIpAddress();
            var form = new PollForm(
                    newPollTitle.getText(),
                    ipAddress,
                    endTime.getValue() == null ? null : LocalDateTime.now().plusMinutes(endTime.getValue()),
                    voteLimit.getText().isEmpty() ? null : Integer.parseInt(voteLimit.getText()),
                    options
            );
            UUID pollId = pollService.create(form);
            var poll = pollService.findById(pollId);
            rmiService.host(ipAddress, new VotingServerImpl(poll, new Host(ipAddress, votingService), new ArrayList<>()));
            if(poll.endTime() != null) {
                pollSchedulingService.schedule(poll);
            }

            MasterLayout.setCurrentPoll(new PollInfo(poll));
            masterLayout.showPage(PollState.class, Page.PollState);

        } catch (UnknownHostException | RemoteException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleJoinPoll() {
        var ipAddress = pollCodeField.getText();

        if (ipAddress.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Please enter a server IP or poll code.");
            return;
        }


        try {
            if(RmiUtils.getLocalIpAddress().equals(ipAddress)) {
                showAlert(Alert.AlertType.ERROR, "Cannot join your own poll.");
                return;
            }
            var server = rmiService.findServer(ipAddress);
            masterLayout.showPage(ActivePoll.class, Page.ActivePoll);
            activePoll.joinPoll(server);
        } catch (MalformedURLException | NotBoundException | RemoteException | UnknownHostException e) {
            masterLayout.showHome();
            showAlert(Alert.AlertType.ERROR, "Something went wrong.");
        }

    }
}