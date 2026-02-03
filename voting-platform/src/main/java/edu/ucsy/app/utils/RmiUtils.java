package edu.ucsy.app.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RmiUtils {

    public static String getLocalIpAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
}
