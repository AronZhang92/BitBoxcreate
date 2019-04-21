package unimelb.bitbox;

import java.io.IOException;
import java.net.ServerSocket;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import unimelb.bitbox.util.Configuration;
import unimelb.bitbox.util.FileSystemManager;
import unimelb.bitbox.util.FileSystemObserver;
import unimelb.bitbox.util.FileSystemManager.FileSystemEvent;

public class ServerMain implements FileSystemObserver {
	private static Logger log = Logger.getLogger(ServerMain.class.getName());
	protected FileSystemManager fileSystemManager;
	
	public ServerMain() throws NumberFormatException, IOException, NoSuchAlgorithmException {
		fileSystemManager=new FileSystemManager(Configuration.getConfigurationValue("path"),this);
	}

	@Override
	public void processFileSystemEvent(FileSystemEvent fileSystemEvent) {
		// TODO: process events
	}
	
//what we wrote, multithreading server 
public static void main(String[] args) {
		ServerSocket listeningSocket = null;
		int ServerNumber=1;
		String portstring = Configuration.getConfigurationValue("port");
		final int port = Integer.parseInt(portstring);
		String maximumconnection = Configuration.getConfigurationValue("maximumIncommingConnections");
		int maxcon = Integer.parseInt(maximumconnection);
		
		 try{
			    listeningSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Could not listen on port " + port);
			System.exit(-1);
		}
			
			//Listen for incoming connections for ever
        if(ServerNumber < maxcon){
            while (true) {
                ServerWorker w;
                try {
                    System.out.println("The server port is:" + port);
                    w = new ServerWorker(listeningSocket.accept(), ServerNumber++);
                    Thread t = new Thread(w);
                    t.start();

                } catch (IOException e) {
                    System.out.println("Accept failed: " + port);
                    System.exit(-1);
                }
            }
        }
}
	
}
