package unimelb.bitbox;

import unimelb.bitbox.util.Document;

import java.util.ArrayList;

public class Connectionlist {
    private static ArrayList<Document> connectionIP = new ArrayList<Document>();
    static int a=0;
    public static void AddNewIPAddress(String address,int port){
        boolean check = false;
        for (Document docu:connectionIP
        ) {
            if(docu.getString("host").equals(address)) {
                check = true;
                break;
            }
        }
        if(!check){
            Document doc = new Document();
            doc.append("host", address);
            doc.append("port", port);
            connectionIP.add(doc);
            a++;
        }

    }

    protected static void removeIPAddress(String address){
        for (Document docu:connectionIP
             ) {
                if(docu.getString("host").equals(address)){
                    System.out.println("The " + address + " has already removed.");
                    connectionIP.remove(docu);
                    break;
                }
        }
    }
    protected static boolean contain(Document doc) {
        if(connectionIP!=null)
            return connectionIP.contains(doc);
        return true;
    }
    public static ArrayList<Document> returnlist(){
        return connectionIP;
    }
    public static int connumber(){
        System.out.println("The connection number is :" + connectionIP.size());
        return a;
    }

}
