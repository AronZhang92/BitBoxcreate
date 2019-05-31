package unimelb.bitbox;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import unimelb.bitbox.util.Configuration;
import unimelb.bitbox.util.Document;

// this class is used to set server socket for client connection

public class SecureServer implements Runnable {
	private static Logger log = Logger.getLogger(ServerMain.class.getName());
	static final int clientport = Integer.parseInt(Configuration.getConfigurationValue("clientPort"));

	@Override
	public void run() {
		System.out.println("secure server running ");
		ServerSocket listeningSocket = null;
		Socket clientSocket = null;
		try {
			listeningSocket = new ServerSocket(clientport);
			while (true) {
				clientSocket = listeningSocket.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
				BufferedWriter out = new BufferedWriter(
						new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
				String clientMsg = null;
				while((clientMsg = in.readLine()) != null) {
					Document clientMsgtoDoc = Document.parse(clientMsg);
					if(clientMsgtoDoc.containsKey("payload")) { // when payload exist
						String payload = clientMsgtoDoc.getString("payload");
						// decode payload
						try {
							AEScrypt.decrypt(payload, function2.getSecreteKey());
							function2.funtional(Document.parse(payload), clientSocket); // analyse command
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else {
						try {
						function2.funtional(Document.parse(clientMsg),clientSocket);
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
					
				}
			}

		} catch (IOException e) {
			log.info("Secure sever could not listen on port " + clientport);
		}
	}
}
