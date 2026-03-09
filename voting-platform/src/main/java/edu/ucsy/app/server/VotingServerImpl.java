package edu.ucsy.app.server;

import edu.ucsy.app.rmi.VotingServer;
import edu.ucsy.app.rmi.VotingService;
import edu.ucsy.app.rmi.dto.*;
import edu.ucsy.app.rmi.dto.output.PollInfo;
import edu.ucsy.app.rmi.event.OnVoteEvent;
import edu.ucsy.app.rmi.event.PollCancelEvent;
import edu.ucsy.app.rmi.event.PollEndEvent;
import edu.ucsy.app.utils.exception.VotingPlatformBusinessException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class VotingServerImpl extends UnicastRemoteObject implements VotingServer {

    private final PollDetail poll;
    private final Host host;
    private final List<Voter> voters;

    public VotingServerImpl(PollDetail poll, Host host, List<Voter> voters) throws RemoteException {
        this.poll = poll;
        this.host = host;
        this.voters = voters;
    }

    @Override
    public void vote(VoteForm form) throws RemoteException {
        validate(form);
        poll.select(form.optionId(), form.ipAddress());

        var event = new OnVoteEvent(
                poll.id(),
                poll.ipAddress(),
                form.optionId(),
                poll.getVotesByOptionId(form.optionId()));

        host.votingService().onVote(event);

        voters.forEach(v -> {
            try {
                v.votingService().onVote(event);
            } catch (RemoteException e) {
                System.out.println("Connection error with voter.");
            }
        });

        checkLimit();
    }

    private void checkLimit() throws RemoteException {
        if(poll.limit() != null && poll.limit() == voters.size()) {
            endPoll();
        }
    }

    private void validate(VoteForm form) {
        if(poll.limit() != null && poll.limit() == poll.total()) {
            throw new VotingPlatformBusinessException("Poll is full.");
        }
        if(!poll.id().equals(form.pollId())) {
            throw new VotingPlatformBusinessException("Invalid poll.");
        }
        if(host.ipAddress().equals(form.ipAddress())) {
            throw new VotingPlatformBusinessException("Poll host cannot vote own poll.");
        }
        voters.stream().filter(v -> v.ipAddress().equals(form.ipAddress()))
                .findFirst().ifPresent(v -> {
                    throw new VotingPlatformBusinessException("A voter can only vote once.");
                });
    }

    @Override
    public PollInfo getPollInfo(String ipAddress, VotingService votingService) throws RemoteException {
        voters.add(new Voter(ipAddress, votingService));
        return new PollInfo(poll, voters.stream().anyMatch(v -> v.ipAddress().equals(ipAddress)));
    }

    @Override
    public void endPoll() throws RemoteException {
        var event = new PollEndEvent(poll);
        host.votingService().onPollEnd(event);
        voters.forEach(v -> {
            try {
                v.votingService().onPollEnd(event);
            } catch (RemoteException e) {
                System.out.println("Connection error with voter.");
            }
        });
    }

    @Override
    public void cancelPoll() throws RemoteException {
        var event = new PollCancelEvent(poll.id(), poll.ipAddress());
        host.votingService().onPollCancel(event);
        voters.forEach(v -> {
            try {
                v.votingService().onPollCancel(event);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
