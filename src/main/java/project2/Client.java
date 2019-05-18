package project2;

import java.net.Socket;

import unimelb.bitbox.util.Configuration;

// this class needed to be move to unimelb.bitbox package after implemented
public class Client {
	String peer = Configuration.getConfigurationValue("clientToPeer");
	Socket socket = null;
}
