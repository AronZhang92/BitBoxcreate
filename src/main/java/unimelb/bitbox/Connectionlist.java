package unimelb.bitbox;


import java.net.Socket;
import java.security.Key;
import java.util.ArrayList;
import java.util.Map;

public class Connectionlist {
    private static ArrayList<Socket> connectionSocket = new ArrayList<Socket>();
    private static Map<Socket, Key> map;
    public static void addNewSocket(Socket socket){
        if(!contain(socket.getInetAddress().toString()))
            connectionSocket.add(socket);
    }
    public static void setkey(Socket socket,Key key){
        map.put(socket,key);
    }
    public static Map<Socket,Key> returnmap(){
        return map;
    }
    public static Key getkey(Socket socket){
        return map.get(socket);
    }
    public static ArrayList<Socket> returnsocketlist(){
        return connectionSocket;
    }
    //check if the ipadress is already in the list.
    public static boolean contain(String ipadress){
        boolean include = false;
        for (Socket socket: connectionSocket
             ) {
<<<<<<< HEAD
            //System.out.println("The check socket is " + socket.getInetAddress().toString());
            if(ipadress.equals(socket.getInetAddress().toString())){
                //System.out.println("now in the equails mode.");
=======
            if(ipadress.equals(socket.getInetAddress().toString())){
>>>>>>> 8ed4405ae3bd2da2a611623c59efef42e665ffbb
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
    public static boolean checkport(Socket socket,int port){
        return false;
    }

}
