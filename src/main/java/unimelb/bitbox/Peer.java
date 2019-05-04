package unimelb.bitbox;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import unimelb.bitbox.util.Configuration;

public class Peer 
{
	private static ServerMain serverMain = null;
	private static Logger log = Logger.getLogger(Peer.class.getName());
    public static void main(String[] args) throws IOException, NumberFormatException, NoSuchAlgorithmException
    {
    	System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tc] %2$s %4$s: %5$s%n");
        log.info("BitBox Peer starting...");
        Configuration.getConfiguration();


        serverMain = new ServerMain();
        Thread t = new Thread(serverMain);
        t.start();


        ClientMain.ClientMain();
        Long starttime = System.currentTimeMillis();
        while(true){
           Long endtime = System.currentTimeMillis();
           if(endtime - starttime == 60000L){
               starttime = endtime;
               synevents.syneventtoall(Connectionlist.returnsocketlist());
           }
        }
    }
    
    public static ServerMain getServerMain() {
    	return serverMain;
    }
}
