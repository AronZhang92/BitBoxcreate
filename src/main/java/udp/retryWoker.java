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
    static int port = 0;

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
                if(threadList.contain(address)){
                    check = false;
                } else{
                    try {
                        datagramSocket.send(datagramPacket);
                    } catch (IOException e) {
                    }
                }
                i ++;
            }else{
                check = false;
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
