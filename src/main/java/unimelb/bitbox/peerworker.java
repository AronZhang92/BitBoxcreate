package unimelb.bitbox;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.simple.JSONObject;
import unimelb.bitbox.util.Document;
import unimelb.bitbox.util.JSONRETURN;

import javax.print.Doc;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class peerworker implements Runnable{
	
	private Socket socket;
	
	public peerworker(Socket socket) {
		this.socket= socket;
	}
	

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
            Document ackreceived = null;
            //while((ackreceived = in.readLine())!=null){
            //    System.out.println("client received a protocol " + ackreceived);
            //}
            Document ack = new Document();
            String frombuffer = in.readLine();
            while(frombuffer == null){
                frombuffer = in.readLine();
            }
            ack = Document.parse(frombuffer);
            System.out.println(ack.toJson());
            if (!frombuffer.equals(JSONRETURN.CONNCECTION_REFUSED().toJson())){
                //System.out.println("received OK");
                System.out.println("Connection established");
                //System.out.println(socket.getInetAddress());
                //While the user input differs from "exit"
                Scanner scanner = new Scanner(System.in);
                String inputStr = null;
                while (!(inputStr = scanner.nextLine()).equals("exit")) {

                    // Send the input string to the server by writing to the socket output stream
                    out.write(inputStr + "\n");
                    out.flush();
                    System.out.println("Message sent");
                    System.out.println("The message sent to " + socket.getInetAddress());
                    // Receive the reply from the server by reading from the socket input stream
                    String received = in.readLine(); // This method blocks until there
                    // is something to read from the
                    // input stream
                    System.out.println("Message received: " + received);
                }

                scanner.close();

            } else {
                System.out.println("The socket closed due to wrong answer :" + frombuffer);
                socket.close();
            }




		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			// Close the socket
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
