package udp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;

public class threadList {
    static DatagramPacket datagramPacket = null;
    static InetAddress address = null;
    static ArrayList<InetAddress> addresses = new ArrayList<>();

    public static void addPacket(DatagramPacket m){
        datagramPacket = m;
        address = m.getAddress();
        addresses.add(address);
        System.out.println("threadList 16: thread list is " + addresses);
    }
 /*   public static boolean contain(InetAddress address){
        boolean answer = false;
        if (addresses.contains(address)){
            answer = true;
            addresses.remove(address);
        }
        return answer;
    }*/

}