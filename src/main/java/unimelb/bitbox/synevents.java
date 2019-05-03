package unimelb.bitbox;

import unimelb.bitbox.util.Document;
import unimelb.bitbox.util.FileSystemManager;
import unimelb.bitbox.util.FileSystemObserver;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class synevents {

    public static void synevent(Socket socket) throws IOException, NoSuchAlgorithmException {
        FileSystemObserver ob = Peer.getServerMain();
        FileSystemManager fsm = new FileSystemManager("share", ob);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
        ArrayList<FileSystemManager.FileSystemEvent> eventslist = fsm.generateSyncEvents();
        for (FileSystemManager.FileSystemEvent event: eventslist
             ) {
            Sendsocket.sendDoc(Sendsocket.sendsocket(event));
        }
    }
}