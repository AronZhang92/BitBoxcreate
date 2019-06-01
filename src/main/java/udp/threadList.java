package udp;

import unimelb.bitbox.util.Document;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;

public class threadList {
    static DatagramPacket datagramPacket = null;
    static InetAddress address = null;
    public static ArrayList<Document> info = new ArrayList<>();
    public static Document infoDoc = new Document();


    public static void addPacket(DatagramPacket m, Document doc){
        datagramPacket = m;
        address = m.getAddress();
        //Document pathname = (Document) doc.get("pathname");
        infoDoc.append("address", address.toString());
        switch (doc.getString("command")){
            case "FILE_CREATE_REQUEST" :
                infoDoc.append("command", "FILE_CREATE_RESPONSE");
                break;
            case "FILE_BYTES_REQUEST" :
                infoDoc.append("command", "FILE_BYTES_RESPONSE");
                break;
            case "FILE_DELETE_REQUEST" :
                infoDoc.append("command", "FILE_DELETE_RESPONSE");
                break;
            case "FILE_MODIFY_REQUEST" :
                infoDoc.append("command", "FILE_MODIFY_RESPONSE");
                break;
            case "DIRECTORY_CREATE_REQUEST" :
                infoDoc.append("command", "DIRECTORY_CREATE_RESPONSE");
                break;
            case "DIRECTORY_DELETE_REQUEST" :
                infoDoc.append("command", "DIRECTORY_DELETE_RESPONSE");
                break;
            case "HANDSHAKE_REQUEST" :
                infoDoc.append("command", "HANDSHAKE_RESPONSE");
                break;
        }

        infoDoc.append("pathname", doc.getString("pathname"));
       // Document newdoc = new Document();
        //newdoc.append("first",infoDoc);
        //newdoc.append("second",doc);
        info.add(infoDoc);

        System.out.println("threadList 16: thread list is " + infoDoc);
    }
    public static boolean contain(Document doc){
        boolean answer = false;
        int n = threadList.info.size();
        for(int j=0; j<n; j++){
            if(threadList.info.get(j).getString("address").equals(doc.getString("address")) &&
            threadList.info.get(j).getString("command").equals(doc.getString("command")) ){
                if (threadList.info.get(j).getString("pathname") != null ){
                    if(threadList.info.get(j).getString("pathname").equals(doc.getString("pathname"))){
                        answer = true;
                        break;
                    }
                } else if(doc.getString("pathname") == null){
                    answer = true;
                    break;
                }

            }
            System.out.println("info" + threadList.info.get(j).toJson());
        }
        return answer;
    }

    public static ArrayList<Document> removePacket(DatagramPacket request, Document doc){
        Document infoReceive = new Document();
        infoReceive.append("address", request.getAddress().toString());
        infoReceive.append("command", doc.getString("command"));
        infoReceive.append("pathname", doc.getString("pathname"));
        info.remove(infoReceive);
        return info;
    }

}
