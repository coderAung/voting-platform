package edu.ucsy.app.rmi;

import edu.ucsy.app.rmi.event.OnVoteEvent;
import edu.ucsy.app.rmi.event.PollEndEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VotingService extends Remote {

    void onVote(OnVoteEvent event) throws RemoteException;

    void onPollEnd(PollEndEvent event) throws RemoteException;
}
