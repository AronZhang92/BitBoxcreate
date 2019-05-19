package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import unimelb.bitbox.util.Document;
import unimelb.bitbox.util.FileSystemManager;

public class udpSendSocket {
	
	 public static Document eventToDoc(FileSystemManager.FileSystemEvent fileSystemEvent){
         FileSystemManager.FileDescriptor fd = fileSystemEvent.fileDescriptor;
         Document doc = new Document();
         Document do1 = new Document();

         if (fd != null) {
             do1 = fd.toDoc();
             doc.append("fileDescriptor",do1);
         }

         doc.append("pathName",fileSystemEvent.pathName);
         doc.append("command",fileSystemEvent.event.toString()+"_REQUEST");
         return doc;
}
	 
	 //still need to be change to mutiple threads 
	  public static void sendToAllPeers(byte[] data) throws IOException {
          ArrayList<String> array = udpConnectionList.getall();
          if (array != null) {
              for (String newstring : array) {
                  String[] middlepeers = newstring.split(":");
                  String address = middlepeers[0];
                  int portnumber = Integer.parseInt(middlepeers[1]);

                  DatagramSocket bSocket = udpPeer.getDatagramSocket();
                  DatagramPacket msg = new DatagramPacket(data, data.length, InetAddress.getByName(address), portnumber);
                  bSocket.send(msg);
              }
          }
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
 
    
