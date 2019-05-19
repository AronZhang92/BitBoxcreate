package unimelb.bitbox;

import unimelb.bitbox.util.FileSystemManager;
import unimelb.bitbox.util.FileSystemObserver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import udp.udpServerMain;

public class synevents {

    public static void synevent(Socket socket) throws IOException, NoSuchAlgorithmException {
        FileSystemObserver ob = Peer.getServerMain();
        FileSystemManager fsm = ServerMain.fileSystemManager;;
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
        ArrayList<FileSystemManager.FileSystemEvent> eventslist = fsm.generateSyncEvents();
        for (FileSystemManager.FileSystemEvent event: eventslist
             ) {
            Sendsocket.sendDoc(Sendsocket.sendsocket(event));
        }
    }
    public static void syneventtoall(ArrayList<Socket> socketlist) throws IOException, NoSuchAlgorithmException {
        if (socketlist.size() ==  0) {
            FileSystemObserver ob = Peer.getServerMain();
            FileSystemManager fsm = ServerMain.fileSystemManager;;
            ArrayList<FileSystemManager.FileSystemEvent> eventslist = fsm.generateSyncEvents();
            for (Socket socket : socketlist
            ) {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
                for (FileSystemManager.FileSystemEvent event : eventslist
                ) {
                    Sendsocket.sendDoc(Sendsocket.sendsocket(event));

                }
            }

        }
    }
}
