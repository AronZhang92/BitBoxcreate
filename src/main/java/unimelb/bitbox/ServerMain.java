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
		
		 try{
			    listeningSocket = new ServerSocket(4444);
			  } catch (IOException e) {
			    System.out.println("Could not listen on port 4444");
			    System.exit(-1);
			  }
			
			//Listen for incoming connections for ever 
			while (true) {
				ServerWorker w;
				try {
					w = new ServerWorker(listeningSocket.accept(), ServerNumber++);
				      Thread t = new Thread(w);
         		      t.start();
					
			} catch(IOException e) {
			      System.out.println("Accept failed: 4444");
			      System.exit(-1); 
				
			}
		
		
	}
}
	
}
