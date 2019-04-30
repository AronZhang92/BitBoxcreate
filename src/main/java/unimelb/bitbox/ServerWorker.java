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
	
	public ServerWorker (Socket client, int clientNumber) {
		this.clientSocket = client ;
		this.clientNumber = clientNumber;
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

                if (frombuffer.equals(JSONRETURN.HANDSHAKE_REQUEST().toJson()) && Connectionlist.connumber() < maxcon){
                    //System.out.println("received request");
                    out.write(JSONRETURN.HANDSHAKE_RESPONSE().toJson() + "\n");
                    out.flush();
                    Connectionlist.AddNewIPAddress(clientSocket.getInetAddress().toString(),clientSocket.getPort());
                    System.out.println("The length of connection list is :" + Connectionlist.connumber());
                    System.out.println("Connection established :" + clientSocket.getInetAddress());
                    System.out.println("Server listening on port " + port + " for a connection");
                    System.out.println("Client conection number " + clientNumber + " accepted:");
                    System.out.println("Remote Port: " + clientSocket.getPort());
                    System.out.println("Remote Hostname: " + clientSocket.getInetAddress().getHostName());
                    System.out.println("Local Port: " + clientSocket.getLocalPort());
                    while((clientMsg = in.readLine())!= null) {
                        doc = Document.parse(clientMsg); //change string to jasonobject

                            System.out.println("The in line is :" + clientMsg);
                            String[] event = doc.getString("event").split(" ");
                            String check = event[0];
                            if(clientMsg.equals(JSONRETURN.HANDSHAKE_REQUEST().toJson())){
                                System.out.println("the request is connection request");
                            }else if(false&&check.equals("DIRECTORY_CREATE")) {//create directory event
                                System.out.println("now in the create file mode.");
                                try {
                                    ServerMain sm = new ServerMain();
                                    sm.fileSystemManager.makeDirectory(doc.getString("name"));
                                } catch (NumberFormatException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (NoSuchAlgorithmException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }
                            else {
                                System.out.println("Message from client " + clientSocket.getInetAddress() + ": " + clientMsg);
                                out.write("Server Ack " + clientMsg + "\n");
                                out.flush();
                                System.out.println("Response sent");
                            }
                        }
                    System.out.println("The connection is lose");
                    Connectionlist.removeIPAddress(clientSocket.getInetAddress().toString());
                } else {
                    out.write(JSONRETURN.CONNCECTION_REFUSED().toJson()+"\n");
                    out.flush();
                    System.out.println("The system get maximum number, try another server.:" + clientSocket.getInetAddress());
                    clientSocket.close();
                }

			}
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
