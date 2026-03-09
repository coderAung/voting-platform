package edu.ucsy.app.rmi;

import edu.ucsy.app.rmi.dto.output.PollInfo;
import edu.ucsy.app.rmi.dto.VoteForm;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VotingServer extends Remote {

    void vote(VoteForm form) throws RemoteException;

    PollInfo getPollInfo(String ipAddress, VotingService votingService) throws RemoteException;

    void endPoll() throws RemoteException;

    void cancelPoll() throws RemoteException;
}
