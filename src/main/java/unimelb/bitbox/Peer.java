package unimelb.bitbox;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import unimelb.bitbox.util.Configuration;

public class Peer 
{
	private static ServerMain serverMain;
	private static Logger log = Logger.getLogger(Peer.class.getName());
    public static void main(String[] args) throws IOException, NumberFormatException, NoSuchAlgorithmException
    {
    	System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tc] %2$s %4$s: %5$s%n");
        log.info("BitBox Peer starting...");
        Configuration.getConfiguration();
        
        new ServerMain();
        ServerMain sm = new ServerMain();
        serverMain =sm;
        Thread t = new Thread(sm);
        t.start();


        //ClientMain.ClientMain();

        
    }
    
    public static ServerMain getServerMain() {
    	return serverMain;
    }
}
