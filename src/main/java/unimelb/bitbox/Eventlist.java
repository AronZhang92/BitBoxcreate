package unimelb.bitbox;

import unimelb.bitbox.util.FileSystemManager;

import java.util.ArrayList;

public class Eventlist {
    static ArrayList<FileSystemManager.FileSystemEvent> eventlist = new ArrayList<FileSystemManager.FileSystemEvent>();
    static ArrayList<Integer> eventlistnumber = new ArrayList<Integer>();
    static ArrayList<String> ipa = new ArrayList<String>();
    static boolean changed = false;
    protected static void addevent(FileSystemManager.FileSystemEvent event){
        eventlist.add(event);
        eventlistnumber.add(0);
        changed = true;
    }
    protected static boolean change(String ipaddress){
        if(changed) {
            if (ipa.contains(ipaddress)) {
                //System.out.println(ipa.size() + "   " + Connectionlist.connumber());
                if (ipa.size() == Connectionlist.connumber()) {
                    ipa = new ArrayList<String>();
                    System.out.println("Int the check node");
                    eventlist.remove(0);
                    changed = false;
                }
                return false;

            } else {
                ipa.add(ipaddress);
                return true;
            }
        }
        return changed;

    }
    protected static FileSystemManager.FileSystemEvent getevent(){
        if (eventlist != null)
            return eventlist.get(0);
        return null;
    }
    protected static void removeevent(FileSystemManager.FileSystemEvent event){
        if(eventlist.contains(event)) {
            eventlist.remove(event);
        }
    }
    protected static void addone(FileSystemManager.FileSystemEvent event){
        if (eventlist.contains(event)){
           int num =  eventlist.indexOf(event);
           eventlistnumber.add(num,eventlistnumber.get(num)+1);
        }
    }

    protected static void check(){
        for (int a:eventlistnumber
             ) {
            if(a == Connectionlist.connumber()){
                int num = eventlist.indexOf(a);
                eventlist.remove(num);
                eventlistnumber.remove(num);
            }
        }
    }

}
