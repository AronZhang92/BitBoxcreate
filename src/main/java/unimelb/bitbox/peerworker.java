package unimelb.bitbox;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.simple.JSONObject;
import unimelb.bitbox.util.Document;
import unimelb.bitbox.util.FileSystemManager;
import unimelb.bitbox.util.JSONRETURN;

import javax.print.Doc;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class peerworker implements Runnable{
	
	private Socket socket;
    private int checkplace = 0;
	
	public peerworker(Socket socket) {
		this.socket= socket;
	}
	String clientMsg;

	public void run() {
		// TODO Auto-generated method stub

		try {
			// Create a stream socket bounded to any port and connect it to the
			// socket bound to localhost on port 4444

			// Get the input/output streams for reading/writing data from/to the socket
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            Document json =JSONRETURN.HANDSHAKE_REQUEST();
            //Three way handshake
            out.write(json.toJson() + "\n");
            out.flush();
            System.out.println("client send a request :" + json.getString("command") + json.toJson());
            Document ack = new Document();
            String frombuffer = in.readLine();
            ack = Document.parse(frombuffer);
            System.out.println(ack.toJson());
            if (ack.getString("command").equals(JSONRETURN.HANDSHAKE_RESPONSE().getString("command"))){
                //System.out.println("received OK");
                System.out.println("Connection established to" + socket.getInetAddress());
                Connectionlist.addNewSocket(socket);
                System.out.println("Successful add ip " + socket.getInetAddress()+ " and port: " + socket.getPort() + "to the connection list");
                while ((clientMsg = in.readLine())!= null) {
                        if (clientMsg.equals("Ack")){
                            System.out.println("Already receive the ack. wait for another action");
                        } else {
                            out.write(clientMsg + "\n");
                            out.flush();
                        }
                    /*if(Eventlist.change(socket.getInetAddress().toString())) {
                        FileSystemManager.FileSystemEvent event = Eventlist.getevent();
                        System.out.println("the event is :"+ event);
                        Document doc = new Document();
                        doc.append("event",event.toString());
                        //doc.append("fileDescriptor",event.fileDescriptor.toDoc());
                        doc.append("pathName", event.pathName);
                        doc.append("path", event.path);
                        doc.append("name", event.name);


                        // Send the input string to the server by writing to the socket output stream
                        out.write(doc.toJson() + "\n");
                        out.flush();

                        System.out.println("Message sent :" + doc.toJson());
                        System.out.println("The message sent to " + socket.getInetAddress());
                        // Receive the reply from the server by reading from the socket input stream
                        String received = in.readLine(); // This method blocks until there
                        // is something to read from the
                        // input stream
                        System.out.println("Message received: " + received);
                    }*/
                }

            } else if (ack.getString("command").equals(JSONRETURN.CONNCECTION_REFUSED().getString("command"))){
                ArrayList<Document> address = (ArrayList<Document>) ack.get("peers");
                if(address!=null){
                    for (Document dou : address) {
                        if(!Connectionlist.contain(dou.getString("IPadress"))){
                            AnotherConnection.AnotherConnection(dou.getString("host"),dou.getInteger("port"));
                            break;
                        }
                    }
                } else {
                    System.out.println("The system return null post, try another one");
                }


            }
            else {
                System.out.println("The socket closed due to wrong answer :" + frombuffer);
                socket.close();
            }
		} catch (UnknownHostException e) {
			e.printStackTrace();
            System.out.println("unkown");
		}catch (ConnectException ce){
            System.out.println("The connection refused.");
        } catch (IOException e) {
			e.printStackTrace();
            System.out.println("ioex");
		}  finally{
			// Close the socket
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
                    System.out.println("another io ex");
				}
			}
		}

	}
}
