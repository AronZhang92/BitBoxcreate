package udp;

import java.net.DatagramSocket;
import java.net.Socket;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class udpConnectionList {
	private static Map<String, Integer> map = new HashMap<String,Integer>();

    public static void addudp(String ipadress, int port) {
        map.put(ipadress, port);
    }

    public static Map<String, Integer> returnmap() {
        return map;
    }

    public static int getport(String ipadress) {
        return map.get(ipadress);
    }

    public static boolean contain(String ipadress) {
    	if(map == null) {
    		return false;
    	}else {
    		return map.containsKey(ipadress);
    	}
    }

    public static void remove(String ipadress) {
        if (map.containsKey(ipadress)) {
            map.remove(ipadress);
        }
    }
    
    public static int getsize() {
    	if(map == null) {
    		return 0;
    	}else {
    		return map.size();
    	}
    }


    public static ArrayList<String> getall(){
        ArrayList<String> array = new ArrayList<String>();
        if(map == null){
            return null;
        }
        Set<String> newset = map.keySet();
        for (String ipadress : newset) {
            String newstring = ipadress + ":" + map.get(ipadress).toString();
            array.add(newstring);
        }
        return array;
    }
}

