package unimelb.bitbox;

import unimelb.bitbox.util.Document;

import java.util.ArrayList;

public class Connectionlist {
    ArrayList<Document> connectionIP = null;

    protected void AddNewIPAddress(String address,int port){

        Document doc = new Document();
        doc.append("host",address);
        doc.append("port",port);
        connectionIP.add(doc);
    }

    protected void removeIPAddress(String address){
        for (Document docu:connectionIP
             ) {

        }
    }

}
