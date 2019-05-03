package unimelb.bitbox;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import unimelb.bitbox.util.Configuration;
import unimelb.bitbox.util.Document;
import unimelb.bitbox.util.FileSystemManager;
import unimelb.bitbox.util.FileSystemObserver;
import unimelb.bitbox.util.FileSystemManager.FileSystemEvent;

public class ServerMain implements FileSystemObserver,Runnable {
	private static Logger log = Logger.getLogger(ServerMain.class.getName());

    static String portstring = Configuration.getConfigurationValue("port");
    static final int port = Integer.parseInt(portstring);
	protected FileSystemManager fileSystemManager;
	
	public ServerMain() throws NumberFormatException, IOException, NoSuchAlgorithmException {
		fileSystemManager=new FileSystemManager(Configuration.getConfigurationValue("path"),this);
	}

	@Override
	public void processFileSystemEvent(FileSystemEvent fileSystemEvent) {
    Sendsocket.sendDoc(Sendsocket.sendsocket(fileSystemEvent));
	}

	public void run(){
        ServerSocket listeningSocket = null;


        try{
            listeningSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Could not listen on port " + port);
            System.exit(-1);
        }
        while (true) {
            ServerWorker w;
            try {
                System.out.println("The server port is:" + port);
                w = new ServerWorker(listeningSocket.accept());
                Thread t = new Thread(w);
                t.start();

            } catch (IOException e) {
                System.out.println("Accept failed: " + port);
                System.exit(-1);
            }
        }


    }


	
}
