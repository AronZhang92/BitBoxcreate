package unimelb.bitbox;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import unimelb.bitbox.util.Configuration;

public class Peer 
{
	private static Logger log = Logger.getLogger(Peer.class.getName());
    public static void main( String[] args ) throws IOException, NumberFormatException, NoSuchAlgorithmException
    {
    	System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tc] %2$s %4$s: %5$s%n");
        log.info("BitBox Peer starting...");
        Configuration.getConfiguration();
        
        new ServerMain();
        
        //what we wrote, multithreading client 
        Socket socket;
    	Socket socket2;
    	try {
    		socket = new Socket("localhost",4444);
    		peerworker w = new peerworker(socket); 
    	    Thread t = new Thread(w);
    	    t.start();
    	    
    	    socket2 = new Socket("10.13.98.67",4444);
    	 	peerworker w2 = new peerworker(socket2); 
    	 	Thread t2 = new Thread(w2);
    	 	t2.start();
    	} catch (UnknownHostException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
        
    }
}
