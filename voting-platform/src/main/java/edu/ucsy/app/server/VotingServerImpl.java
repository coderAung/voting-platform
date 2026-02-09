package edu.ucsy.app.server;

import edu.ucsy.app.rmi.VotingServer;
import edu.ucsy.app.rmi.dto.*;
import edu.ucsy.app.rmi.event.OnVoteEvent;
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
        voters.add(new Voter(form.ipAddress(), form.votingService()));

        var event = new OnVoteEvent(
                poll.id(),
                form.optionId(),
                poll.getVotesByOptionId(form.optionId()));

        host.votingService().onVote(event);

        voters.forEach(v -> {
            try {
                v.votingService().onVote(event);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void validate(VoteForm form) {
        if(poll.limit() != null && poll.limit() == voters.size()) {
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
    public PollInfo getPollInfo() throws RemoteException {
        return new PollInfo(poll);
    }
}
