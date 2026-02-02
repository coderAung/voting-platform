package edu.ucsy.app.rmi;

import edu.ucsy.app.rmi.dto.PollDetail;
import edu.ucsy.app.rmi.dto.VoteForm;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VotingServer extends Remote {

    void vote(VoteForm form) throws RemoteException;

    PollDetail getPollDetail() throws RemoteException;
}
