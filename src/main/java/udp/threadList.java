package udp;

import unimelb.bitbox.util.Document;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;

public class threadList {
    static DatagramPacket datagramPacket = null;
    static InetAddress address = null;
    static ArrayList<Document> info = new ArrayList<>();
    static Document infoDoc = new Document();


    public static void addPacket(DatagramPacket m, Document doc){
        datagramPacket = m;
        address = m.getAddress();
        //Document pathname = (Document) doc.get("pathname");
        infoDoc.append("address", address.toString());
        infoDoc.append("command", doc.getString("command"));
        infoDoc.append("pathname", doc.getString("pathname"));
       // Document newdoc = new Document();
        //newdoc.append("first",infoDoc);
        //newdoc.append("second",doc);
        info.add(infoDoc);

        System.out.println("threadList 16: thread list is " + infoDoc);
    }
 /*   public static boolean contain(InetAddress address){
        boolean answer = false;
        if (addresses.contains(address)){
            answer = true;
            addresses.remove(address);
        }
        return answer;
    }*/

    public static ArrayList<Document> removePacket(DatagramPacket request){
        if ()
    }

}
