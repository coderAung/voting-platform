package edu.ucsy.app.ui.controller;

import edu.ucsy.app.rmi.dto.output.OptionInfo;
import edu.ucsy.app.server.entities.Poll;
import edu.ucsy.app.server.service.PollManagementService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class PollState {

    private final PollManagementService pollService;
    private final MasterLayout masterLayout;

    @FXML
    private Label pollTitleLabel;
    @FXML private Label pollDescriptionLabel;
    @FXML private VBox optionsContainer;

    @FXML private Label ipAddressLabel;

    private final ToggleGroup voteGroup = new ToggleGroup();

    @FXML
    public void initialize() {
        var currentPoll = MasterLayout.getCurrentPoll();
        ipAddressLabel.setText(currentPoll.ipAddress());

        pollTitleLabel.setText(currentPoll.title());
        pollDescriptionLabel.setText("Total options: " + currentPoll.options().size());

        refresh(currentPoll.options());
    }

    private void refresh(List<OptionInfo> options) {
        optionsContainer.getChildren().clear();

        options.forEach(option -> {
            var hbox = new HBox();
            hbox.getStyleClass().add("option-row");
            hbox.getChildren().add(new Label(option.title()));

            var region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            hbox.getChildren().add(region);

            hbox.getChildren().add(new Label("%s".formatted(option.votes())));

            optionsContainer.getChildren().add(hbox);

        });
    }

    @FXML
    void closePoll() {
        pollService.changeStatus(MasterLayout.getCurrentPoll().id(), Poll.Status.Cancel);
        MasterLayout.setCurrentPoll(null);
        masterLayout.showHome();
    }
}
