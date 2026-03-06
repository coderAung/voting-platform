package edu.ucsy.app.server.service;

import edu.ucsy.app.rmi.VotingServer;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

@Service
public class RmiService {

    private static final int PORT = 1099;

    private static Registry registry;
    private static VotingServer server;

    public void host(String ipAddress, VotingServer server) throws RemoteException, MalformedURLException {
        registry = LocateRegistry.createRegistry(PORT);
        RmiService.server = server;
        Naming.rebind("rmi://%s:%s/%s".formatted(ipAddress, PORT, "server"), server);
    }

    public VotingServer findServer(String ipAddress) throws MalformedURLException, NotBoundException, RemoteException {
        return (VotingServer) Naming.lookup("rmi://%s:%s/%s".formatted(ipAddress, PORT, "server"));
    }

    public void cleanUp(String ipAddress) throws MalformedURLException, NotBoundException, RemoteException {
        Naming.unbind("rmi://%s:%s/%s".formatted(ipAddress, PORT, "server"));
        UnicastRemoteObject.unexportObject(server, true);
        UnicastRemoteObject.unexportObject(registry, true);
        server = null;
        registry = null;
    }
}
