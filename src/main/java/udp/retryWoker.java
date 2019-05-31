package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;

public class retryWoker implements Runnable{
    static DatagramPacket datagramPacket = null;
    static DatagramSocket datagramSocket = null;
    static InetAddress address = null;

    public retryWoker(DatagramPacket m, DatagramSocket socket) {
        datagramPacket = m;
        address = m.getAddress();
        datagramSocket = socket;
    }

    public void run() {
        boolean check = true;
        int i = 0;
        while (check){
            if(i < 2){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(threadList.addresses.contains(address)){
                    try {
                        datagramSocket.send(datagramPacket);
                        System.out.println("\n \n resend once !!!!!!!!!!!!!! \n \n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else{
                   break;
                }
                i ++;
            }

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
