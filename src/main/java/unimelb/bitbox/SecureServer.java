package unimelb.bitbox;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import unimelb.bitbox.util.Configuration;

public class SecureServer implements Runnable{
	 private static Logger log = Logger.getLogger(ServerMain.class.getName());
	 static final int clientport = Integer.parseInt(Configuration.getConfigurationValue("clientport"));
	
	@Override
	public void run() {
		ServerSocket listeningSocket = null;
		Socket clientSocket = null;
        try{
            listeningSocket = new ServerSocket(clientport);
            clientSocket = listeningSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
            
            
        } catch (IOException e) {
            log.info("Secure sever could not listen on port " + clientport);
        }   	
	}
}
