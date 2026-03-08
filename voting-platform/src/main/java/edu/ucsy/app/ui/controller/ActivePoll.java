package edu.ucsy.app.ui.controller;

import edu.ucsy.app.rmi.VotingServer;
import edu.ucsy.app.rmi.VotingService;
import edu.ucsy.app.rmi.dto.VoteForm;
import edu.ucsy.app.rmi.dto.output.PollInfo;
import edu.ucsy.app.ui.DateTimeUtils;
import edu.ucsy.app.utils.RmiUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.net.UnknownHostException;
import java.rmi.RemoteException;

@Controller
@RequiredArgsConstructor
public class ActivePoll {

    private final VotingService votingService;

    @FXML private Button submitBtn;
    @FXML private Label pollTitleLabel;
    @FXML private Label pollDescriptionLabel;
    @FXML private VBox optionsContainer;
    @FXML private Label ipAddressLabel;
    @FXML private Label voteEndTimeLabel;

    private final ToggleGroup voteGroup = new ToggleGroup();

    public void joinPoll(VotingServer server) throws RemoteException {

        var currentPoll = server.getPollInfo();
        ipAddressLabel.setText(currentPoll.ipAddress());
        if(currentPoll.endTime() != null) {
            voteEndTimeLabel.setText("%s : %s".formatted("Vote ends at", DateTimeUtils.formatTime(currentPoll.endTime().toLocalTime())));
        }

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

        submitBtn.setOnAction(ev -> {
            try {
                handleVoteSubmit(currentPoll, server);
            } catch (RemoteException | UnknownHostException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void handleVoteSubmit(PollInfo poll, VotingServer server) throws RemoteException, UnknownHostException {
        if (voteGroup.getSelectedToggle() == null) {
            System.out.println("No option selected!");
            return;
        }
        ToggleButton selected = (ToggleButton) voteGroup.getSelectedToggle();
        String optionId = (String) selected.getUserData();
        var form = new VoteForm(poll.id(), optionId, RmiUtils.getLocalIpAddress(), votingService);
        server.vote(form);
    }

    public void refresh(String optionId, int votes) {
        var box = (HBox) voteGroup.getToggles().stream()
                .map(t -> (ToggleButton) t)
                .filter(t -> t.getUserData().equals(optionId))
                .findFirst().orElseThrow()
                .getParent();
        ((Label) box.getChildren().getLast()).setText("%s".formatted(votes));
    }
}