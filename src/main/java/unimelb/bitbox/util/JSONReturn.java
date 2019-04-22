package unimelb.bitbox.util;

import java.security.NoSuchAlgorithmException;
import java.security.Timestamp;
import java.util.ArrayList;

import static unimelb.bitbox.util.MD5.getMd5;

public class JSONReturn {

    private Document document = new Document();
    public static Document INVALID_PROTOCOL(){
        Document doc = new Document();
        doc.append("command", "INVALID_PROTOCOL");
        doc.append("message", "message must contain a command field as string");
        return doc;
    }
    public Document addDocument(String a, String b){
        Document doc = new Document();
        doc.append("command",a);
        doc.append("message",b);
        return doc;
    }

    public void addexist(String a,String b){
        if (!document.containsKey(a)){
            document.append(a,b);
        } else {

        }
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

    public Document HANDSHAKE_REQUEST (){
        Document doc = new Document();
        doc.append("command", "HANDSHAKE_REQUEST");
        Document doc1 = new Document();
        doc1.append("host", "localhost");
        doc1.append("port", 8111);
        doc.append("hostPort", doc1);
        return doc;
    }

    public Document HANDSHAKE_RESPONSE (){
        Document doc = new Document();
        doc.append("command", "HANDSHAKE_RESPONSE");
        Document doc1 = new Document();
        doc1.append("host", "10.13.187.213");
        doc1.append("port", 1234);
        doc.append("hostPort", doc1);
        return doc;
    }

    public static Document FILE_CREATE_REQUEST (String a, String b, long c,/*timestamp*/ long d, String e) {
        Document doc = new Document();
        doc.append("command", /*"FILE_CREATE_REQUEST"*/a);
        Document doc1 = new Document();
        doc1.append("md5", getMd5(b));
        doc1.append("lastModified", c);
        doc1.append("fileSize", d);
        doc.append("fileDescriptor", doc1);
        doc.append("pathName", e);
        return doc;
    }

    public Document FILE_CREATE_RESPONSE (String a, String b, long c, long d, String e, String f, boolean g){
        Document doc = new Document();
        doc.append("command", /*"FILE_CREATE_RESPONSE"*/a);
        Document doc1 = new Document();
        doc1.append("md5", getMd5(b));
        doc1.append("lastModified", c);
        doc1.append("fileSize", d);
        doc.append("fileDescriptor", doc1);
        doc.append("pathName", e);
        doc.append("message", f);
        doc.append("status", g);
        return doc;
    }

    public Document FILE_BYTES_REQUEST (String a, String b, long c, long d, String e, int f, int g) {
        Document doc = new Document();
        doc.append("command", /*"FILE_BYTES_REQUEST"*/a);
        Document doc1 = new Document();
        doc1.append("md5", getMd5(b));
        doc1.append("lastModified", c);
        doc1.append("fileSize", d);
        doc.append("fileDescriptor", doc1);
        doc.append("pathName", e);
        doc.append("position", f);
        doc.append("length", g);
        return doc;
    }

    public Document FILE_BYTES_RESPONSE (String a, String b, long c, long d, String e, int f, int g,
                                         String h, String i, boolean j) {
        Document doc = new Document();
        doc.append("command", /*"FILE_BYTES_RESPONSE"*/a);
        Document doc1 = new Document();
        doc1.append("md5", getMd5(b));
        doc1.append("lastModified", c);
        doc1.append("fileSize", d);
        doc.append("fileDescriptor", doc1);
        doc.append("pathName", e);
        doc.append("position", f);
        doc.append("length", g);
        doc.append("content", h);
        doc.append("message", i);
        doc.append("status", j);
        return doc;
    }

    public Document FILE_DELETE_REQUEST (String a, String b, long c, long d, String e) {
        Document doc = new Document();
        doc.append("command", /*"FILE_DELETE_REQUEST"*/a);
        Document doc1 = new Document();
        doc1.append("md5", getMd5(b));
        doc1.append("lastModified", c);
        doc1.append("fileSize", d);
        doc.append("fileDescriptor", doc1);
        doc.append("pathName", e);
        return doc;
    }

    public Document FILE_DELETE_RESPONSE (String a, String b, long c, long d, String e, String f, boolean g) {
        Document doc = new Document();
        doc.append("command", /*"FILE_DELETE_REQUEST"*/a);
        Document doc1 = new Document();
        doc1.append("md5", getMd5(b));
        doc1.append("lastModified", c);
        doc1.append("fileSize", d);
        doc.append("fileDescriptor", doc1);
        doc.append("pathName", e);
        doc.append("message", f);
        doc.append("status", g);
        return doc;
    }

    public Document FILE_MODIFY_REQUEST (String a, String b, long c, long d, String e) {
        Document doc = new Document();
        doc.append("command", /*"FILE_MODIFY_REQUEST"*/a);
        Document doc1 = new Document();
        doc1.append("md5", getMd5(b));
        doc1.append("lastModified", c);
        doc1.append("fileSize", d);
        doc.append("fileDescriptor", doc1);
        doc.append("pathName", e);
        return doc;
    }

    public Document FILE_MODIFY_RESPONSE (String a, String b, long c, long d, String e, String f, boolean g) {
        Document doc = new Document();
        doc.append("command", /*"FILE_MODIFY_REQUEST"*/a);
        Document doc1 = new Document();
        doc1.append("md5", getMd5(b));
        doc1.append("lastModified", c);
        doc1.append("fileSize", d);
        doc.append("fileDescriptor", doc1);
        doc.append("pathName", e);
        doc.append("message", f);
        doc.append("status", g);
        return doc;
    }

    public static Document DIRECTORY_CREATE_REQUEST(String a, String b){
        Document doc = new Document();
        doc.append("command", /*"DIRECTORY_CREATE_REQUEST"*/a);
        doc.append("pathName", b);
        return doc;
    }

    public static Document DIRECTORY_CREATE_RESPONSE(String a, String b, String c, boolean d){
        Document doc = new Document();
        doc.append("command", /*"DIRECTORY_CREATE_REQUEST"*/a);
        doc.append("pathName", b);
        doc.append("message", c);
        doc.append("status", d);
        return doc;
    }

    public static Document DIRECTORY_DELETE_REQUEST(String a, String b){
        Document doc = new Document();
        doc.append("command", /*"DIRECTORY_DELETE_REQUEST"*/a);
        doc.append("pathName", b);
        return doc;
    }

    public static Document DIRECTORY_DELETE_RESPONSE(String a, String b, String c, boolean d){
        Document doc = new Document();
        doc.append("command", /*"DIRECTORY_DELETE_REQUEST"*/a);
        doc.append("pathName", b);
        doc.append("message", c);
        doc.append("status", d);
        return doc;
    }
}
