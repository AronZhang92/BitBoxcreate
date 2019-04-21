package unimelb.bitbox;

import com.sun.org.apache.xpath.internal.operations.Bool;

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
			System.out.println("Connection established");

            System.out.println(socket.toString());
			// Get the input/output streams for reading/writing data from/to the socket
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));


            //Three way handshake
            out.write("Request");
            boolean ack = true;
            while (ack){
                String ackreceived = in.readLine();
                if (ackreceived.equals("ack")){
                    ack = false;
                }
            }


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
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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
