package unimelb.bitbox;

import unimelb.bitbox.util.*;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class ServerWorker implements Runnable {
	private Socket clientSocket;
	private String maximumconnection = Configuration.getConfigurationValue("maximumIncommingConnections");
	private int maxcon = Integer.parseInt(maximumconnection);
    private static Logger log = Logger.getLogger(ServerWorker.class.getName());
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
                if(Connectionlist.contain(clientSocket.getInetAddress().toString())){
                    log.info("Already in the connection list.... close connection");
                    clientSocket.close();
                }
				if (doc.getString("command").equals("HANDSHAKE_REQUEST")
						&& Connectionlist.connum() < maxcon) {
					 log.info("received request from : " + clientSocket.getInetAddress());
					out.write(JSONRETURN2
							.HANDSHAKE_RESPONSE(clientSocket.getInetAddress().toString(), clientSocket.getPort())
							.toJson() + "\n");
					out.flush();
					Connectionlist.addNewSocket(clientSocket);
					log.info("Connect to the" + clientSocket.getInetAddress().toString());
                    synevents.synevent(clientSocket);
					while ((clientMsg = in.readLine()) != null && Connectionlist.containsocket(clientSocket)) {
                        try {
                            function2.funtional(Document.parse(clientMsg),clientSocket); //send jason object to class funtional
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

					}
					log.info("The connection is lose");
					Connectionlist.remove(clientSocket);
					clientSocket.close();
				} else {
					out.write(JSONRETURN2.CONNCECTION_REFUSED().toJson() + "\n");
					out.flush();
					log.info(
							"The system get maximum number, try another server.:" + clientSocket.getInetAddress());
					clientSocket.close();
				}

			} catch (SocketException e) {
				log.info("closed due to the socket exception");
			} catch (NoSuchAlgorithmException e) {
                log.info("Can't use the algorithm write in the project");
            }
            clientSocket.close();
		}catch (SocketException e){
            log.info("Connection disconnect due to the socket Exception");
        } catch (IOException e) {
			log.info("Can not build a socket with due to the IOException");
		}
	}

}
