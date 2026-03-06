package edu.ucsy.app.ui.controller;

import edu.ucsy.app.rmi.dto.output.OptionInfo;
import edu.ucsy.app.server.entities.Poll;
import edu.ucsy.app.server.service.PollManagementService;
import edu.ucsy.app.server.service.RmiService;
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

    @FXML private Label pollTitleLabel;
    @FXML private Label pollDescriptionLabel;
    @FXML private VBox optionsContainer;

    @FXML private Label ipAddressLabel;
    @FXML private Label voteEndTimeLabel;

    @FXML
    public void initialize() {
        var currentPoll = MasterLayout.getCurrentPoll();
        ipAddressLabel.setText(currentPoll.ipAddress());
        if(currentPoll.endTime() != null) {
            voteEndTimeLabel.setText("%s : %s".formatted("Vote ends at", currentPoll.endTime()));
        }

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
            rmiService.cleanUp(MasterLayout.getCurrentPoll().ipAddress());
            pollService.changeStatus(MasterLayout.getCurrentPoll().id(), Poll.Status.Cancel);
            MasterLayout.setCurrentPoll(null);
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();

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
