package unimelb.bitbox;

import unimelb.bitbox.util.Configuration;
import unimelb.bitbox.util.Document;
import unimelb.bitbox.util.FileSystemManager;
import unimelb.bitbox.util.JSONRETURN;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;

public class ServerWorker implements Runnable{
	private Socket clientSocket;
	private int clientNumber;
    String portstring = Configuration.getConfigurationValue("port");
    final int port = Integer.parseInt(portstring);
    private String maximumconnection = Configuration.getConfigurationValue("maximumIncommingConnections");
    private int maxcon = Integer.parseInt(maximumconnection);
	
	public ServerWorker (Socket client) {
		this.clientSocket = client ;
	}
	
	public void run() {
		    try {
		    //System.out.println("Server listening on port " + port + " for a connection");
			//System.out.println("Client conection number " + clientNumber + " accepted:");
			//System.out.println("Remote Port: " + clientSocket.getPort());
			//System.out.println("Remote Hostname: " + clientSocket.getInetAddress().getHostName());
			//System.out.println("Local Port: " + clientSocket.getLocalPort());
			
			//Get the input/output streams for reading/writing data from/to the socket
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));


			//Read the message from the client and reply
			//Notice that no other connection can be accepted and processed until the last line of 
			//code of this loop is executed, incoming connections have to wait until the current
			//one is processed unless...we use threads!
			String clientMsg = null;
            Document ack = null;
			boolean con = false;
			try {
			    //while((ack = in.readLine())!=null) {
                //    System.out.println("received ack :" + ack);
                //}
                //System.out.println("In the received mode");


                String frombuffer = in.readLine();
                Document doc = new Document();

                if (frombuffer.equals(JSONRETURN.HANDSHAKE_REQUEST(clientSocket.getInetAddress().toString(),clientSocket.getPort()).toJson()) && Connectionlist.connum() < maxcon){
                    //System.out.println("received request");
                    out.write(JSONRETURN.HANDSHAKE_RESPONSE(clientSocket.getInetAddress().toString(),clientSocket.getPort()).toJson() + "\n");
                    out.flush();
                    if (Connectionlist.contain(clientSocket.getInetAddress().toString())){

                    } else if(clientSocket.getInetAddress().toString().equals("/127.0.0.1")){
                        System.out.println("This is the localhost port");

                    }  else {
                        System.out.println("The socket is " + clientSocket.getInetAddress()+"the port is "+ clientSocket.getPort());
                        Connectionlist.addNewSocket(clientSocket);
                        ClientMain.ClientMain(clientSocket.getInetAddress().toString(),clientSocket.getPort());
                    }
                    System.out.println("The length of connection list is :" + Connectionlist.connum());
                    System.out.println("Connection established :" + clientSocket.getInetAddress());
                    System.out.println("Server listening on port " + port + " for a connection");
                    System.out.println("Client conection number " + clientNumber + " accepted:");
                    System.out.println("Remote Port: " + clientSocket.getPort());
                    System.out.println("Remote Hostname: " + clientSocket.getInetAddress().getHostName());
                    System.out.println("Local Port: " + clientSocket.getLocalPort());
                    while((clientMsg = in.readLine())!= null) {
                        doc = Document.parse(clientMsg); //change string to jasonobject

                            System.out.println("The in line is :" + clientMsg);
                            String event = doc.getString("event");
                            if(clientMsg.equals(JSONRETURN.HANDSHAKE_REQUEST(clientSocket.getInetAddress().toString(),clientSocket.getPort()).toJson())){
                                System.out.println("the request is connection request");
                            }
                            else {
                                System.out.println("Message from client " + clientSocket.getInetAddress() + ": " + clientMsg);
                                Funtional.funtional(Document.parse(clientMsg));
                            }
                        }
                    System.out.println("The connection is lose");
                    Connectionlist.remove(clientSocket);
                } else {
                    out.write(JSONRETURN.CONNCECTION_REFUSED().toJson()+"\n");
                    out.flush();
                    System.out.println("The system get maximum number, try another server.:" + clientSocket.getInetAddress());
                    clientSocket.close();
                }

			}
			catch(SocketException e) {
				System.out.println("closed...");
			} catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
                clientSocket.close();
	} catch (SocketException ex) {
		ex.printStackTrace();
	}catch (IOException e) {
		e.printStackTrace();
	} 	
	}

}
