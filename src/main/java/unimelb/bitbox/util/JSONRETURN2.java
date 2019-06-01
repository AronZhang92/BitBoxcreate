package unimelb.bitbox.util;

import unimelb.bitbox.Connectionlist;

import java.net.Socket;
import java.util.ArrayList;

public class JSONRETURN2 {


    public static Document INVALID_PROTOCOL(){
        Document doc = new Document();
        doc.append("command", "INVALID_PROTOCOL");
        doc.append("message", "message must contain a command field as string");
        return doc;
    }

    public static Document CONNCECTION_REFUSED (){ //waiting for changing 
        Document doc = new Document();
        doc.append("command", "CONNECTION_REFUSED");
        doc.append("message", "connection limit reached");
        ArrayList<Document> docs = new ArrayList<Document>();
        Document doc1 = new Document();
        for (Socket socket: Connectionlist.returnsocketlist()
        ) {
            doc.append("host",socket.getInetAddress().toString());
            doc.append("port",socket.getPort());
            docs.add(doc1);
        }
        doc.append("peers", docs);
        doc.toJson();
        return doc;
    }


    public static Document HANDSHAKE_REQUEST (String ipadress,int host){
        Document doc = new Document();
        doc.append("command", "HANDSHAKE_REQUEST");
        Document doc1 = new Document();
        doc1.append("host", ipadress);
        doc1.append("port", host);
        doc.append("hostPort", doc1);
        return doc;
    }

    public static Document HANDSHAKE_RESPONSE (String ipadress,int host){
        Document doc = new Document();
        doc.append("command", "HANDSHAKE_RESPONSE");
        Document doc1 = new Document();
        doc1.append("host", ipadress);
        doc1.append("port", host);
        doc.append("hostPort", doc1);
        return doc;
    }

    public static Document FILE_BYTES_REQUEST(Document fileDescripor, String pathName,Long position,Long length) {
        Document doc = new Document();
        doc.append("command", "FILE_BYTES_REQUEST");
        doc.append("fileDescriptor", fileDescripor);
        doc.append("pathName", pathName);
        doc.append("position", position);
        doc.append("length", length);
        return doc;
    }
    public static Document FILE_DELETE_REQUEST(Document fileDescripor, String pathName) {
        Document doc = new Document();
        doc.append("command", "FILE_DELETE_REQUEST");
        doc.append("fileDescriptor", fileDescripor);
        doc.append("pathName", pathName);
        return doc;
    }
    public static Document FILE_DELETE_RESPONSE(Document fileDescripor, String pathName,String message,boolean status) {
        Document doc = new Document();
        doc.append("command", "FILE_DELETE_RESPONSE");
        doc.append("fileDescriptor", fileDescripor);
        doc.append("pathName", pathName);
        doc.append("message", message);
        doc.append("status", status);
        return doc;
    }

    public static Document DIRECTORY_DELETE_RESPONSE( String pathName,String message,boolean status) {
        Document doc = new Document();
        doc.append("command", "DIRECTORY_DELETE_RESPONSE");
        doc.append("pathName", pathName);
        doc.append("message", message);
        doc.append("status", status);
        return doc;
    }
    public static Document FILE_BYTES_RESPONSE(Document fileDescripor, String pathName,String bytes,String message,Boolean status,Long position,Long length) {
        Document doc = new Document();
        doc.append("command", "FILE_BYTES_RESPONSE");
        doc.append("fileDescriptor", fileDescripor);
        doc.append("pathName", pathName);
        doc.append("content",bytes);
        doc.append("position", position);
        doc.append("length", length);
        doc.append("message", message);
        doc.append("status", status);
        return doc;
    }

    public static Document FILE_CREATE_RESPONSE(Document fileDescripor, String pathName, String message, Boolean status,Long position) {
        Document doc = new Document();
        doc.append("command", "FILE_CREATE_RESPONSE");
        doc.append("fileDescriptor", fileDescripor);
        doc.append("pathName", pathName);
        doc.append("position", position);
        doc.append("length", fileDescripor.getLong("fileSize"));
        doc.append("message", message);
        doc.append("status", status);
        return doc;
    }
    
    public static Document FILE_MODIFY_REQUEST(Document fileDescripot, String pathName) {
    	 Document doc = new Document();
         doc.append("command", "FILE_MODIFY_REQUEST");
         doc.append("pathName", pathName);
         return doc;
    }

    public static Document FILE_MODIFY_RESPONSE(Document fileDescripor, String pathName, String message, boolean status) {
        Document doc = new Document();
        doc.append("command", "FILE_MODIFY_RESPONSE");
        doc.append("fileDescriptor", fileDescripor);
        doc.append("pathName", pathName);
        doc.append("message", message);
        doc.append("status", status);
        return doc;
    }
    
    public static Document DIRECTORY_CREATE_RESPONSE(String pathName, String message, boolean status) {
    	Document doc = new Document();
        doc.append("command", "DIRECTORY_CREATE_RESPONSE");
        doc.append("pathName", pathName);
        doc.append("message", message);
        doc.append("status", status);
        return doc;
    }
    
    public static Document AUTH_RESPONSE(String key, boolean status, String message) {
    	Document doc = new Document();
    	doc.append("command", "DIRECTORY_CREATE_RESPONSE");
    	doc.append("AES128", key);
    	doc.append("status", status);
    	doc.append("message", message);
    	return doc;
    }
    
    public static Document AUTH_RESPONSE( boolean status, String message) {
    	Document doc = new Document();
    	doc.append("command", "DIRECTORY_CREATE_RESPONSE");
    	doc.append("status", status);
    	doc.append("message", message);
    	return doc;
    }
    
    public static Document LIST_PEERS_RESPONSE() {
    	Document doc = new Document();
    	Document peerList = new Document();
    	ArrayList<Socket> peers = Connectionlist.returnsocketlist();
    	doc.append("command","LIST_PEERS_RESPONSE");
    	for(Socket peer:peers) {
    		peerList.append("host", peer.getInetAddress().toString());
    		peerList.append("prot", peer.getPort());
    	}
    	doc.append("peers", peerList.toJson());
    	return doc;
    }
    
    public static Document CONNECT_PEER_RESPONSE(String address, String port, boolean status, String message) {
    	Document doc = new Document();
    	doc.append("command", "CONNECT_PEER_RESPONSE");
    	doc.append("host", "address");
    	doc.append("port", port);
    	doc.append("status", status);
    	doc.append("message", message);
    	return doc;
    }

}
