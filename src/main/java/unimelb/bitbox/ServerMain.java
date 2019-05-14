package unimelb.bitbox;

import java.io.IOException;
import java.net.ServerSocket;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import unimelb.bitbox.util.Configuration;
import unimelb.bitbox.util.FileSystemManager;
import unimelb.bitbox.util.FileSystemObserver;
import unimelb.bitbox.util.FileSystemManager.FileSystemEvent;

public class ServerMain implements FileSystemObserver,Runnable {
	private static Logger log = Logger.getLogger(ServerMain.class.getName());

    static String portstring = Configuration.getConfigurationValue("port");
    static final int port = Integer.parseInt(portstring);
	protected static FileSystemManager fileSystemManager;
	
	public ServerMain() throws NumberFormatException, IOException, NoSuchAlgorithmException {
		fileSystemManager=new FileSystemManager(Configuration.getConfigurationValue("path"),this);
	}

    public static FileSystemManager returnfilesm(){
	    return fileSystemManager;
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
            log.info("Could not listen on port " + port);
        }

        while (true) {
            ServerWorker w;
            try {
                if(Connectionlist.contain(listeningSocket.getInetAddress().toString())){
                    log.info("Already connected");
                } else {
                    w = new ServerWorker(listeningSocket.accept());
                    Thread t = new Thread(w);
                    t.start();
                }

            } catch (IOException e) {
               log.info("Accept failed: " + port);
                System.exit(-1);
            }
        }


    }


	
}
