package unimelb.bitbox;

import unimelb.bitbox.util.Configuration;
import unimelb.bitbox.util.Document;
import unimelb.bitbox.util.FileSystemManager;
import unimelb.bitbox.util.JSONRETURN;

import javax.print.Doc;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;

public class ServerWorker implements Runnable {
	private Socket clientSocket;
	private int clientNumber;
	String portstring = Configuration.getConfigurationValue("port");
	final int port = Integer.parseInt(portstring);
	private String maximumconnection = Configuration.getConfigurationValue("maximumIncommingConnections");
	private int maxcon = Integer.parseInt(maximumconnection);

	public ServerWorker(Socket client) {
		this.clientSocket = client;
	}

	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));

			String clientMsg = null;
			Document ack = null;
			boolean con = false;
			try {
				String frombuffer = in.readLine();
				Document doc = Document.parse(frombuffer);

				if (doc.getString("command").equals("HANDSHAKE_REQUEST")
						&& Connectionlist.connum() < maxcon) {
					// System.out.println("received request");
					out.write(JSONRETURN
							.HANDSHAKE_RESPONSE(clientSocket.getInetAddress().toString(), clientSocket.getPort())
							.toJson() + "\n");
					out.flush();
					Connectionlist.addNewSocket(clientSocket);
                    synevents.synevent(clientSocket);
					while ((clientMsg = in.readLine()) != null) {
                        try {
                            Funtional.funtional(Document.parse(clientMsg)); //send jason object to class funtional
                        } catch (NoSuchAlgorithmException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

					}
					System.out.println("The connection is lose");
					Connectionlist.remove(clientSocket);
				} else {
					out.write(JSONRETURN.CONNCECTION_REFUSED().toJson() + "\n");
					out.flush();
					System.out.println(
							"The system get maximum number, try another server.:" + clientSocket.getInetAddress());
					clientSocket.close();
				}

			} catch (SocketException e) {
				System.out.println("closed...");
			} catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            clientSocket.close();
		} catch (SocketException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
