package edu.ucsy.app.ui.controller;

import edu.ucsy.app.rmi.dto.output.PollInfo;
import edu.ucsy.app.server.service.PollManagementService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ActivePoll {

    private final PollManagementService pollService;

    @FXML private Label pollTitleLabel;
    @FXML private Label pollDescriptionLabel;
    @FXML private VBox optionsContainer;

    @FXML private Label ipAddressLabel;

    private final ToggleGroup voteGroup = new ToggleGroup();

    @FXML
    public void initialize() { }

    // Called from Home after navigation
    public void loadPoll(UUID pollId) {
        MasterLayout.setCurrentPoll(new PollInfo(pollService.findById(pollId)));
        var currentPoll = MasterLayout.getCurrentPoll();
        ipAddressLabel.setText(currentPoll.ipAddress());

        pollTitleLabel.setText(currentPoll.title());
        pollDescriptionLabel.setText("Total options: " + currentPoll.options().size());

        optionsContainer.getChildren().clear();
        currentPoll.options().forEach(option -> {
            HBox box = new HBox();
            box.getStyleClass().add("toggle-box");

            ToggleButton btn = new ToggleButton(option.title());
            btn.setToggleGroup(voteGroup);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.getStyleClass().add("my-toggle");
            btn.setUserData(option.id());
            HBox.setHgrow(btn, Priority.ALWAYS);
            box.getChildren().add(btn);
            box.getChildren().add(new Label("%s".formatted(option.votes())));
            optionsContainer.getChildren().add(box);
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