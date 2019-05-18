package unimelb.bitbox;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;
import udp.udp;

import unimelb.bitbox.util.Configuration;

public class Peer 
{
	private static ServerMain serverMain = null;
	private static SecureServer secureServer = null;
	private static Logger log = Logger.getLogger(Peer.class.getName());
	
	public static void main(String[] args) throws NumberFormatException, NoSuchAlgorithmException, IOException {
		// decide which mode is used
		String mode = Configuration.getConfigurationValue("mode");
		if(mode.equals("tcp")) {
			Peer.tcp();
		} else {
			udp.udpMode();
		}
	}
	
	
	
    public static void tcp() throws IOException, NumberFormatException, NoSuchAlgorithmException
    {
    	System.out.println("tcp mode started");
    	System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tc] %2$s %4$s: %5$s%n");
        log.info("BitBox Peer starting...");
         Configuration.getConfiguration();
         String timeString = Configuration.getConfigurationValue("syncInterval");
         //syn time the time that the program need to be syn
         Long syntime = Long.parseLong(timeString)*1000;


        serverMain = new ServerMain();
        Thread t = new Thread(serverMain);
        t.start();
        
        //create thread for secure server 
        secureServer= new SecureServer();
        Thread h = new Thread(secureServer);
        h.start();
        


        ClientMain.ClientMain();
        Long starttime = System.currentTimeMillis();
        while(true){
           Long endtime = System.currentTimeMillis();
           if(endtime - starttime == syntime){
               starttime = endtime;
               synevents.syneventtoall(Connectionlist.returnsocketlist());
           }
        }
    }
    
    public static ServerMain getServerMain() {
    	return serverMain;
    }
}
