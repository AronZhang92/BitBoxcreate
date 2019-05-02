package unimelb.bitbox.util;

import unimelb.bitbox.Connectionlist;

import java.net.Socket;
import java.util.ArrayList;

public class JSONRETURN {

    public static Document INVALID_PROTOCOL(){
        Document doc = new Document();
        doc.append("command", "INVALID_PROTOCOL");
        doc.append("message", "message must contain a command field as string");
        return doc;
    }

    public static Document CONNCECTION_REFUSED (){
        Document doc = new Document();
        doc.append("command", "CONNECTION_REFUSED");
        doc.append("message", "connection limit reached");
        ArrayList<Document> docs = new ArrayList<Document>();
        Document doc1 = new Document();
        for (Socket socket: Connectionlist.returnsocketlist()
             ) {
            doc.append("host",socket.getInetAddress().toString());
            doc.append("host",socket.getPort());
            docs.add(doc1);
        }
        doc.append("peers", docs);
        doc.toJson();
        return doc;
    }


    public static Document HANDSHAKE_REQUEST (String ipadress,int host){
        Document doc = new Document();
        doc.append("command", "HANDSHAKE_REQUEST");
        ArrayList<Document> docs = new ArrayList<Document>();
        Document doc1 = new Document();
        doc1.append("host", ipadress);
        doc1.append("port", host);
        doc.append("hostPort", docs);
        return doc;
    }

    public static Document HANDSHAKE_RESPONSE (String ipadress,int host){
        Document doc = new Document();
        doc.append("command", "HANDSHAKE_RESPONSE");
        ArrayList<Document> docs = new ArrayList<Document>();
        Document doc1 = new Document();
        doc1.append("host", ipadress);
        doc1.append("port", host);
        doc.append("hostPort", docs);
        return doc;
    }


}
