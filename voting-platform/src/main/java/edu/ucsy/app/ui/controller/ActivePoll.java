package edu.ucsy.app.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivePoll {

    @FXML private Label pollTitleLabel;
    @FXML private Label pollDescriptionLabel;
    @FXML private VBox optionsContainer;

    private final ToggleGroup voteGroup = new ToggleGroup();

    @FXML
    public void initialize() {
        // Assign toggle group to all existing toggle buttons
        optionsContainer.getChildren().forEach(node -> {
            if (node instanceof ToggleButton btn) {
                btn.setToggleGroup(voteGroup);
            }
        });
    }

    @FXML
    private void handleVoteSubmit() {
        if (voteGroup.getSelectedToggle() == null) {
            System.out.println("No option selected!");
            return;
        }
        ToggleButton selected = (ToggleButton) voteGroup.getSelectedToggle();
        System.out.println("Voted for: " + selected.getText());
        // RMI call will go here later
    }

    public void refresh(String title, String description) {
        pollTitleLabel.setText(title);
        pollDescriptionLabel.setText(description);
    }
}