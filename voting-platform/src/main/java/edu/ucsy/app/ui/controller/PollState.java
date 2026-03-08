package edu.ucsy.app.ui.controller;

import edu.ucsy.app.rmi.dto.output.OptionInfo;
import edu.ucsy.app.server.entities.Poll;
import edu.ucsy.app.server.service.PollManagementService;
import edu.ucsy.app.server.service.PollSchedulingService;
import edu.ucsy.app.server.service.RmiService;
import edu.ucsy.app.ui.DateTimeUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class PollState {

    private final RmiService rmiService;
    private final MasterLayout masterLayout;
    private final PollManagementService pollService;
    private final PollSchedulingService pollSchedulingService;

    @FXML private Label pollTitleLabel;
    @FXML private Label pollDescriptionLabel;
    @FXML private VBox optionsContainer;

    @FXML private Label ipAddressLabel;
    @FXML private Label voteEndTimeLabel;

    @FXML
    public void initialize() {
        var currentPoll = MasterLayout.getCurrentPoll();
        ipAddressLabel.setText(currentPoll.ipAddress());
        voteEndTimeLabel.setText("Vote ends at : %s%nVote limits : %s".formatted(DateTimeUtils.formatTime(currentPoll.endTime()), currentPoll.limit() == null ? "" : currentPoll.limit()));

        pollTitleLabel.setText(currentPoll.title());
        pollDescriptionLabel.setText("Total options: " + currentPoll.options().size());

        refresh(currentPoll.options());
    }

    private void refresh(List<OptionInfo> options) {
        optionsContainer.getChildren().clear();

        options.forEach(option -> {
            var hbox = new HBox();
            hbox.setUserData(option.id());
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
        try {
            var currentPoll = MasterLayout.getCurrentPoll();
            pollService.changeStatus(currentPoll.id(), Poll.Status.Cancel);
            pollSchedulingService.remove(currentPoll.id());
            MasterLayout.setCurrentPoll(null);
            if(currentPoll.limit() != null && currentPoll.limit() == currentPoll.total()) {
                rmiService.findServer(currentPoll.ipAddress()).endPoll();
            } else {
               rmiService.findServer(currentPoll.ipAddress()).cancelPoll();
            }
            rmiService.cleanUp(currentPoll.ipAddress());

        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
        masterLayout.showHome();
    }

    public void refresh(String optionId, int votes) {
        var label = (Label) optionsContainer.getChildren().stream().filter(c -> c.getUserData().equals(optionId))
                .findFirst().map(c -> (HBox) c).orElseThrow()
                .getChildren().getLast();
        label.setText("%s".formatted(votes));
    }
}
