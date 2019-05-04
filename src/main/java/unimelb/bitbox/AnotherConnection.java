package unimelb.bitbox;

import unimelb.bitbox.util.Document;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class AnotherConnection {

    public static void AnotherConnection(ArrayList<Document> address){
        try {
            for (Document dou : address) {
                if(dou != null) {
                    int port = dou.getInteger("port");
                    String IPaddress = dou.getString("IPadress");
                    if (!Connectionlist.contain(dou.getString("IPadress"))) {
                        Socket socket = new Socket(IPaddress, port);
                        peerworker w = new peerworker(socket);
                        Thread t = new Thread(w);
                        t.start();
                        break;
                    } else {
                        System.out.println("The system return null post, try another one");
                    }
                }else{
                    System.out.println("wrong document, try again");
                }
            }
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
