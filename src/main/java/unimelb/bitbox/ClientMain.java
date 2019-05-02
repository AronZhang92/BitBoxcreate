package unimelb.bitbox;

import unimelb.bitbox.util.Configuration;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientMain {
    static boolean done = false;
    public static void ClientMain(String ipadress, int portnumber) {
        Socket socket;
        try {
        	System.out.println("The target ipadress is "+ ipadress.substring(1));
            socket = new Socket(ipadress.substring(1),portnumber);
            peerworker w = new peerworker(socket);
            Thread t = new Thread(w);
            t.start();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void ClientMain(){
        //what we wrote, multithreading client
        Socket socket;
        String peer = Configuration.getConfigurationValue("peers");
        String[] peers = peer.split(";");
        String address = "";
        int portnumber = 0;
        int i = 0;
        while (i < peers.length){
            String[] middlepeers = peers[i].split(":");
            address = middlepeers[0];
            portnumber = Integer.parseInt(middlepeers[1]);
            System.out.println("the address is " + address + "\n the port number is " + portnumber);
            try {
                socket = new Socket(address,portnumber);
                peerworker w = new peerworker(socket);
                Thread t = new Thread(w);
                t.start();

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            i++;
        }

    }



}
