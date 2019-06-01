package udp;

import unimelb.bitbox.util.Document;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;

public class retryWoker implements Runnable{
    static DatagramPacket datagramPacket = null;
    static DatagramSocket datagramSocket = null;
    static InetAddress address = null;
    Document infoSend = new Document();

    public retryWoker(DatagramPacket m, DatagramSocket socket, Document doc) {
        datagramPacket = m;
        address = m.getAddress();
        datagramSocket = socket;

        infoSend.append("address", address.toString());
        infoSend.append("command", doc.getString("command"));
        infoSend.append("pathname", doc.getString("command"));
    }

    public void run() {
        System.out.println("retryworker21 : new thread start");
        boolean check = true;
        int i = 0;
        while (check){
            if(i < 2){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(threadList.info.contains(infoSend)){
                    try {
                        datagramSocket.send(datagramPacket);
                        String msg = new String(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength());
                        System.out.println("resend once !!!!!!!!!!!!!!" + msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else{
                   break;
                }
                i ++;
            }
            System.out.println("retryworker44 : one thread closed");
            //wait 2 s
            // if contain (address)
                // check = false
        }
//        ip port,
//                receive pacet
//                        check
//                                list cun thread
//                duiying nage thread
//                command response
//                        close thread
    }
}
