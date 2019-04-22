package unimelb.bitbox;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class AnotherConnection {

    public static void Anotheronnection(String address,int port){
        try {
            Socket socket = new Socket(address,port);
            peerworker w = new peerworker(socket);
            Thread t = new Thread(w);
            t.start();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
