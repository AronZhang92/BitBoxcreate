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
        infoSend.append("pathname", doc.getString("pathname"));
    }

    public Document trandfer(Document doc) {
        doc = infoSend;
        switch (doc.getString("command")) {
            case "FILE_CREATE_REQUEST":
                doc.append("command", "FILE_CREATE_RESPONSE");
                break;
            case "FILE_BYTES_REQUEST":
                doc.append("command", "FILE_BYTES_RESPONSE");
                break;
            case "FILE_DELETE_REQUEST":
                doc.append("command", "FILE_DELETE_RESPONSE");
                break;
            case "FILE_MODIFY_REQUEST":
                doc.append("command", "FILE_MODIFY_RESPONSE");
                break;
            case "DIRECTORY_CREATE_REQUEST":
                doc.append("command", "DIRECTORY_CREATE_RESPONSE");
                break;
            case "DIRECTORY_DELETE_REQUEST":
                doc.append("command", "DIRECTORY_DELETE_RESPONSE");
                break;
            case "HANDSHAKE_REQUEST":
                doc.append("command", "HANDSHAKE_RESPONSE");
                break;
        }
        System.out.println("doc" + doc.toJson());
        return doc;
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
                System.out.println("infosend" + trandfer(infoSend).toJson());
                int n = threadList.info.size();
                for(int j=0; j<n; j++){
                    System.out.println("info" + threadList.info.get(j).toJson());
                }
                System.out.println("contain" + threadList.contain(trandfer(infoSend)));
                if(threadList.contain(trandfer(infoSend))){
                    try {
                        datagramSocket.send(datagramPacket);
                        String msg = new String(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength());
                        System.out.println("resend once !!!!!!!!!!!!!!" + msg);
                        i ++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else{
                    check = false;
                }
            }else {
                threadList.info.remove(trandfer(infoSend));
                udpConnectionList.remove(address.toString());
                check = false;
            }
            System.out.println("retryworker44 : one thread closed");
        }
    }
}
