package edu.ucsy.app.handler;

import edu.ucsy.app.rmi.event.OnVoteEvent;
import edu.ucsy.app.rmi.event.PollEndEvent;
import edu.ucsy.app.server.entities.Poll;
import edu.ucsy.app.server.service.PollManagementService;
import edu.ucsy.app.server.service.PollSchedulingService;
import edu.ucsy.app.server.service.RmiService;
import edu.ucsy.app.server.service.VoteManagementService;
import edu.ucsy.app.ui.controller.ActivePoll;
import edu.ucsy.app.ui.controller.MasterLayout;
import edu.ucsy.app.ui.controller.PollState;
import edu.ucsy.app.utils.RmiUtils;
import javafx.application.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class VotingEventHandler {

    private final PollState pollState;
    private final ActivePoll activePoll;
    private final MasterLayout masterLayout;
    private final RmiService rmiService;
    private final PollManagementService pollService;
    private final VoteManagementService voteService;
    private final PollSchedulingService pollSchedulingService;

    @EventListener
    void handle(OnVoteEvent event) {
        try {
            var local = RmiUtils.getLocalIpAddress();
            if(event.pollIpAddress().equals(local)) {
                Platform.runLater(() -> pollState.refresh(event.optionId(), event.votes()));
            } else {
                Platform.runLater(() -> activePoll.refresh(event.optionId(), event.votes()));
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

    }

    @EventListener
    void handle(PollEndEvent event) {
        try {
            final var poll = event.poll();
            if(LocalDateTime.now().isBefore(poll.endTime())) {
                pollSchedulingService.remove(poll.id());
            }
            final var ipAddress = RmiUtils.getLocalIpAddress();
            if(!poll.ipAddress().equals(ipAddress)) {
                pollService.create(poll, false);
            } else {
                pollService.changeStatus(poll.id(), Poll.Status.Finished);
            }
            voteService.bulkCreate(poll.options());
            MasterLayout.setCurrentPoll(null);
            rmiService.cleanUp(ipAddress);

            Platform.runLater(masterLayout::showHistory);

        } catch (UnknownHostException | MalformedURLException | NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
