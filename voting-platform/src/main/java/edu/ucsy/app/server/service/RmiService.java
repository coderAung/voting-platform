package edu.ucsy.app.server.service;

import edu.ucsy.app.rmi.VotingServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RmiService {

    private static final int PORT = 1099;

    public void host(String ipAddress, VotingServer server) throws RemoteException, MalformedURLException {
        LocateRegistry.createRegistry(PORT);
        Naming.rebind("rmi://%s:%s/%s".formatted(ipAddress, PORT, "server"), server);
    }

    public void cleanUp(String ipAddress) {

    }
}
