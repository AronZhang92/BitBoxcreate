package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;

import unimelb.bitbox.util.Configuration;
import unimelb.bitbox.util.Document;
import unimelb.bitbox.util.FileSystemManager;

public class udpSendSocket {
	
	 public static Document eventToDoc(FileSystemManager.FileSystemEvent fileSystemEvent) {
         FileSystemManager.FileDescriptor fd = fileSystemEvent.fileDescriptor;
         Document doc = new Document();
         Document do1 = new Document();

         if (fd != null) {
             do1 = fd.toDoc();
             doc.append("fileDescriptor", do1);
         }

         doc.append("pathName", fileSystemEvent.pathName);
         doc.append("command", fileSystemEvent.event.toString() + "_REQUEST");
         return doc;
     }
	 
	 //still need to be change to mutiple threads 
	  public static void sendToAllPeers(byte[] data) throws IOException {
          ArrayList<String> array = udpConnectionList.getall();
          if (array != null) {
              for (String newstring : array) {
                  String[] middlepeers = newstring.split(":");
                  String address = middlepeers[0].replace("/", "");
                  int portnumber = Integer.parseInt(middlepeers[1]);

                  DatagramSocket bSocket = udpPeer.getDatagramSocket();
                  DatagramPacket msg = new DatagramPacket(data, data.length, InetAddress.getByName(address), portnumber);

                  String a = new String(data);
                  Document doc = Document.parse(a);
                  retryWoker re = new retryWoker(msg, bSocket, doc);
                  threadList.addPacket(msg, doc);
                  System.out.println("udpSendSocket 43: use method addpacket 1");
                  bSocket.send(msg);
                  Thread t = new Thread(re);
                  t.start();
              }
          }
  }
	
	  public static void sendToConfigPeers(byte[] data) throws IOException {
		  ArrayList<String> array = new ArrayList<String>();
		  String peerList = Configuration.getConfigurationValue("peers");
		  String[] peers = peerList.split(",");
		  for(String peer: peers) {
			  array.add(peer);
		  }
		  
          if (array != null) {
              for (String newstring : array) {
                  String[] middlepeers = newstring.split(":");
                  String address = middlepeers[0];
                  int portnumber = Integer.parseInt(middlepeers[1]);

                  DatagramSocket bSocket = udpPeer.getDatagramSocket();
                  DatagramPacket msg = new DatagramPacket(data, data.length, InetAddress.getByName(address), portnumber);

                  String a = new String(data);
                  Document doc = Document.parse(a);
                  retryWoker re = new retryWoker(msg, bSocket, doc);
                  threadList.addPacket(msg, doc);
                  bSocket.send(msg);
                  Thread t = new Thread(re);
                  t.start();
              }
          }
	  }
	  
	  public static void sendToPeers(byte[] data, ArrayList<String> array) throws IOException {
		  
          if (array != null) {
              for (String newstring : array) {
                  String[] middlepeers = newstring.split(":");
                  String address = middlepeers[0];
                  int portnumber = Integer.parseInt(middlepeers[1]);

                  DatagramSocket bSocket = udpPeer.getDatagramSocket();
                  DatagramPacket msg = new DatagramPacket(data, data.length, InetAddress.getByName(address), portnumber);

                  String a = new String(data);
                  Document doc = Document.parse(a);
                  retryWoker re = new retryWoker(msg, bSocket, doc);
                  threadList.addPacket(msg, doc);
                  bSocket.send(msg);
                  Thread t = new Thread(re);
                  t.start();
              }
          }
	  }
	  
	  public static void connectToPeer(String address, int port) throws IOException {
		          byte[] handShake = udpSendSocket.doctoByte(udpJSONRETURN.HANDSHAKE_REQUEST(address, port));
		          DatagramPacket msg = new DatagramPacket(handShake, handShake.length, InetAddress.getByName(address),port);
		          udpPeer.getDatagramSocket().send(msg);
		          
		          String a = new String(handShake);
                  Document doc = Document.parse(a);
                  retryWoker re = new retryWoker(msg, udpPeer.getDatagramSocket(), doc);
                  threadList.addPacket(msg, doc);
                  Thread t = new Thread(re);
                  t.start();
	  }
	  
	  //send to one peer
	  public static void sendtosocket(byte[] data, InetAddress address, int port) throws IOException {
	        DatagramSocket bSocket = udpPeer.getDatagramSocket();
	        DatagramPacket msg = new DatagramPacket(data, data.length, address,port);
	        bSocket.send(msg);
	    }
	 
	 public static byte[] doctoByte(Document doc){
	        byte[] m = doc.toJson().getBytes();
	        return m;
	    }
	 
	  public static String getIpFromAddress(String newstring) {
		  String[] middlepeers = newstring.split(":");
          String address = middlepeers[0];
          return address;
	  }
	  
	  public static int getPortFromAddress(String newstring) {
		  String[] middlepeers = newstring.split(":");
		  int portnumber = Integer.parseInt(middlepeers[1]);
		  return portnumber;
	  }
}
 
    
