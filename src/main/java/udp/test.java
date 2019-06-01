package udp;

import unimelb.bitbox.util.Document;

import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class test {
	public static void main(String[] args) {
		ArrayList<String> array = new ArrayList<>();
        Document doc1 = new Document();
        Document doc2 = new Document();
        
        doc1.append("key", "value");
        doc2.append("key", "value");
        array.add(doc1.toJson());
        System.out.println(array.get(0));
        array.remove(doc2.toJson());
        System.out.println(array.get(0));
	}
    
   
    
}
