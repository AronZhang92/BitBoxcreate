package unimelb.bitbox.util;

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
        Document doc2 = new Document();
        Document doc1 = new Document();
        doc1.append("host", "localhost");
        doc1.append("port", 8111);
        doc2.append("host", "10.13.187.213");
        doc2.append("port", 1234);
        docs.add(doc1);
        docs.add(doc2);
        // Document doc3 = new Document();
        doc.append("peers", docs);
        doc.toJson();
        return doc;
    }


    public static Document HANDSHAKE_REQUEST (){
        Document doc = new Document();
        doc.append("command", "HANDSHAKE_REQUEST");
        ArrayList<Document> docs = new ArrayList<Document>();
        Document doc1 = new Document();
        doc1.append("host", "localhost");
        doc1.append("port", 8111);
        doc.append("hostPort", docs);
        return doc;
    }

    public static Document HANDSHAKE_RESPONSE (){
        Document doc = new Document();
        doc.append("command", "HANDSHAKE_RESPONSE");
        ArrayList<Document> docs = new ArrayList<Document>();
        Document doc1 = new Document();
        doc1.append("host", "10.13.187.213");
        doc1.append("port", 1234);
        doc.append("hostPort", docs);
        return doc;
    }
    
    public static Document FILE_BYTES_REQUEST(String md5, long position, long length) {
    	Document doc = new Document();
    	doc.append("event", "FILE_BYTES_REQUEST");
    	doc.append("md5", md5);
    	doc.append("start", Long.toString(position));  //notice the key of start position
    	doc.append("fileSize", Long.toString(length));
    	return doc;
    }


}
