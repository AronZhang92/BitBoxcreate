package unimelb.bitbox;

import java.io.IOException;
import java.net.Socket;

public class cmdfunction {
    public static void connectionlist(){
        for (Socket socket : Connectionlist.returnsocketlist()
        ) {
            System.out.println(socket.getInetAddress());
        }
    }
    public static void connectionto(String address,int port) throws IOException {
        
        Socket  socket = new Socket(address, port);
        peerworker w = new peerworker(socket);
        Thread t = new Thread(w);
        t.start();
    }
    public static void disconnectionto(String address) throws IOException {
        if(Connectionlist.contain(address)){
            for (Socket socket : Connectionlist.returnsocketlist()
            ) {
                if (socket.getInetAddress().toString().equals(address)){
                    socket.close();
                    break;
                }
            }
        }
    }
}

