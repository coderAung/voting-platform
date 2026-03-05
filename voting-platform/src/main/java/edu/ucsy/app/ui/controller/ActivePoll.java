package edu.ucsy.app.ui.controller;

import edu.ucsy.app.rmi.dto.PollDetail;
import edu.ucsy.app.server.service.PollManagementService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ActivePoll {

    private final PollManagementService pollService;

    @FXML private Label pollTitleLabel;
    @FXML private Label pollDescriptionLabel;
    @FXML private VBox optionsContainer;

    private final ToggleGroup voteGroup = new ToggleGroup();
    private PollDetail currentPoll;

    @FXML
    public void initialize() { }

    // Called from Home after navigation
    public void loadPoll(UUID pollId) {
        currentPoll = pollService.findById(pollId);

        pollTitleLabel.setText(currentPoll.title());
        pollDescriptionLabel.setText("Total options: " + currentPoll.options().size());

        optionsContainer.getChildren().clear();
        currentPoll.options().forEach(option -> {
            ToggleButton btn = new ToggleButton(option.title());
            btn.setToggleGroup(voteGroup);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.getStyleClass().add("choice-box");
            btn.setUserData(option.id());
            optionsContainer.getChildren().add(btn);
        });
    }

    @FXML
    private void handleVoteSubmit() {
        if (voteGroup.getSelectedToggle() == null) {
            System.out.println("No option selected!");
            return;
        }
        ToggleButton selected = (ToggleButton) voteGroup.getSelectedToggle();
        String optionId = (String) selected.getUserData();
        System.out.println("Voted for option: " + optionId);
    }
}