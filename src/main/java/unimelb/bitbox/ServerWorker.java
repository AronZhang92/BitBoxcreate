package unimelb.bitbox;

import unimelb.bitbox.util.Configuration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

public class ServerWorker implements Runnable{
	private Socket clientSocket;
	private int clientNumber;
    String portstring = Configuration.getConfigurationValue("port");
    final int port = Integer.parseInt(portstring);
	
	public ServerWorker (Socket client, int clientNumber) {
		this.clientSocket = client ;
		this.clientNumber = clientNumber;
	}
	
	public void run() {
		    try {
		    System.out.println("Server listening on port " + port + " for a connection");
			System.out.println("Client conection number " + clientNumber + " accepted:");
			System.out.println("Remote Port: " + clientSocket.getPort());
			System.out.println("Remote Hostname: " + clientSocket.getInetAddress().getHostName());
			System.out.println("Local Port: " + clientSocket.getLocalPort());
			
			//Get the input/output streams for reading/writing data from/to the socket
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));


			//Read the message from the client and reply
			//Notice that no other connection can be accepted and processed until the last line of 
			//code of this loop is executed, incoming connections have to wait until the current
			//one is processed unless...we use threads!
			String clientMsg = null;
			try {

			while((clientMsg = in.readLine()) != null) {
				System.out.println("Message from client " + clientSocket.getInetAddress() + ": " + clientMsg);
				out.write("Server Ack " + clientMsg + "\n");
				out.flush();
				System.out.println("Response sent");
			}}
			catch(SocketException e) {
				System.out.println("closed...");
			}
			clientSocket.close();
	} catch (SocketException ex) {
		ex.printStackTrace();
	}catch (IOException e) {
		e.printStackTrace();
	} 	
	}

}
