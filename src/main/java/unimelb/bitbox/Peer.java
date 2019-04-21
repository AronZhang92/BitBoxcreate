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
        String peers = Configuration.getConfigurationValue("peers");
        String[] middlepeers = peers.split(":");
        String address = middlepeers[0];
        int portnumber = Integer.parseInt(middlepeers[1]);
        System.out.println("the address is " + address + "\n the port number is " + portnumber);
    	try {
    		socket = new Socket(address,portnumber);
    		peerworker w = new peerworker(socket); 
    	    Thread t = new Thread(w);
    	    t.start();

    	} catch (UnknownHostException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
        
    }
}
