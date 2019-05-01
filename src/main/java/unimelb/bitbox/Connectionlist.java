package unimelb.bitbox;

import unimelb.bitbox.util.Document;

import java.net.Socket;
import java.util.ArrayList;

public class Connectionlist {
    private static ArrayList<Socket> connectionSocket = new ArrayList<Socket>();
    static int a=0;
    public static void addNewSocket(Socket socket){
        connectionSocket.add(socket);
    }
    public static ArrayList<Socket> returnsocketlist(){
        return connectionSocket;
    }
    public static boolean contain(String ipadress){
        boolean include = false;
        for (Socket socket: connectionSocket
             ) {
            if(ipadress.equals(socket.getInetAddress().toString())){
                include = true;
                break;
            }
        }
        return include;
    }
    public static int connum(){
        return connectionSocket.size();
    }
    public static void remove(Socket socket){
        if(connectionSocket.contains(socket)){
            connectionSocket.remove(socket);
        }
    }

}
