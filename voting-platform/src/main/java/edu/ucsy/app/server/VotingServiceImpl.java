package edu.ucsy.app.server;

import edu.ucsy.app.rmi.VotingService;
import edu.ucsy.app.rmi.event.OnVoteEvent;
import edu.ucsy.app.rmi.event.PollEndEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

@Component
public class VotingServiceImpl extends UnicastRemoteObject implements VotingService {

    private final ApplicationEventPublisher publisher;

    public VotingServiceImpl(ApplicationEventPublisher publisher) throws RemoteException {
        this.publisher = publisher;
    }

    @Override
    public void onVote(OnVoteEvent event) throws RemoteException {
        publisher.publishEvent(event);
    }

    @Override
    public void onPollEnd(PollEndEvent event) throws RemoteException {
        publisher.publishEvent(event);
    }
}
