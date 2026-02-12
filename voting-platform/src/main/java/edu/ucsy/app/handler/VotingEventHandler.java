package edu.ucsy.app.handler;

import edu.ucsy.app.rmi.event.OnVoteEvent;
import edu.ucsy.app.rmi.event.PollEndEvent;
import edu.ucsy.app.server.service.PollManagementService;
import edu.ucsy.app.server.service.PollSchedulingService;
import edu.ucsy.app.server.service.VoteManagementService;
import edu.ucsy.app.utils.RmiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;

import java.net.UnknownHostException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class VotingEventHandler {

    private final PollManagementService pollService;
    private final VoteManagementService voteService;
    private final PollSchedulingService pollSchedulingService;

    void handle(OnVoteEvent event) {

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
                voteService.bulkCreate(poll.id(), poll.options());
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
