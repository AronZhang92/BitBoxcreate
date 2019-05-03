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
    //check if the ipadress is already in the list.
    public static boolean contain(String ipadress){
        boolean include = false;
        //System.out.println("The target ipadress is "+ ipadress);
        for (Socket socket: connectionSocket
             ) {
            System.out.println("The check socket is " + socket.getInetAddress().toString());
            if(ipadress.equals(socket.getInetAddress().toString())){
                System.out.println("now in the equails mode.");
                include = true;
                break;
            }
        }
        return include;
    }
    //return how many connection are set
    public static int connum(){
        return connectionSocket.size();
    }
    public static void remove(Socket socket){

        if(connectionSocket.isEmpty() || !connectionSocket.contains(socket)){

        } else {
            connectionSocket.remove(socket);
        }
    }

}
