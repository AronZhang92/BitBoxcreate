package unimelb.bitbox;

import unimelb.bitbox.util.Document;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Connectionlist {
    private static ArrayList<Socket> connectionSocket = new ArrayList<Socket>();
    private static ArrayList<BufferedWriter> albw = new ArrayList<BufferedWriter>();
    static int a=0;
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
        //System.out.println("The target ipadress is "+ ipadress);
        for (Socket socket: connectionSocket
             ) {
            //System.out.println("The check socket is " + socket.getInetAddress().toString());
            if(ipadress.equals(socket.getInetAddress().toString())){
                //System.out.println("now in the equails mode.");
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
    public static void addnewoutput(BufferedWriter out){
        albw.add(out);
    }
    public static ArrayList<BufferedWriter> returnoutputstream(){
        return albw;
    }
    public static boolean containoutput(BufferedWriter out){
        return albw.contains(out);
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
