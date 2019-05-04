package unimelb.bitbox;


import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Connectionlist {
    private static ArrayList<Socket> connectionSocket = new ArrayList<Socket>();
    public static void addNewSocket(Socket socket){
        if(!contain(socket.getInetAddress().toString()))
            connectionSocket.add(socket);
    }
    public static ArrayList<Socket> returnsocketlist(){
        return connectionSocket;
    }
    //check if the ipadress is already in the list.
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
    //check if the ipadress is already in the list.
    public static boolean containsocket(Socket socket){
        return connectionSocket.contains(socket);
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
