package udp;

import java.io.IOException;
import java.net.DatagramSocket;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import unimelb.bitbox.ClientMain;
import unimelb.bitbox.Connectionlist;
import unimelb.bitbox.Peer;
import unimelb.bitbox.SecureServer;
import unimelb.bitbox.synevents;
import unimelb.bitbox.util.Configuration;

public class udpPeer {
	private static udpServerMain udpServerMain = null;
	private static SecureServer secureServer = null;
	private static Logger log = Logger.getLogger(Peer.class.getName());
	private static DatagramSocket socket;

	public static void udpMode() throws NumberFormatException, NoSuchAlgorithmException, IOException {
		socket = new DatagramSocket(Integer.parseInt(Configuration.getConfigurationValue("udpProt")));

		System.out.println("tcp mode started");
		System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tc] %2$s %4$s: %5$s%n");
		log.info("BitBox Peer starting...");
		Configuration.getConfiguration();
		String timeString = Configuration.getConfigurationValue("syncInterval");
		// syn time the time that the program need to be syn
		Long syntime = Long.parseLong(timeString) * 1000;

		udpServerMain = new udpServerMain(); // changing
		Thread t = new Thread(udpServerMain);
		t.start();

		// create thread for secure server
		secureServer = new SecureServer();
		Thread h = new Thread(secureServer);
		h.start();

		udpSendSocket.sendToAllPeers(udpSendSocket.doctoByte(
				udpJSONRETURN.HANDSHAKE_REQUEST(socket.getLocalAddress().toString(), socket.getLocalPort())));
	}

	public static udpServerMain getServerMain() {
		return udpServerMain;
	}

	public static DatagramSocket getDatagramSocket() {
		return socket;
	}
}
