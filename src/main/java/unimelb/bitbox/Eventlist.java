package unimelb.bitbox;

import unimelb.bitbox.util.Document;
import java.util.ArrayList;

public class Eventlist {
    private static ArrayList<Document> documentslist = new ArrayList<Document>();
    public static void addDocument(Document doc){
        documentslist.add(doc);
    }
    public static boolean contain(Document doc1){
        boolean include = false;
        for (Document doc: documentslist
             ) {
         if(doc1.getString("commond").equals(doc.getString("commond"))){
             if(doc1.getString("pathName").equals(doc.getString("pathName"))){
                 include = true;
             }
         }
        }
    return include;
    }
    public static void remove(Document doc){
        documentslist.remove(doc);
    }


}
