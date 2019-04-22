package unimelb.bitbox.util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import unimelb.bitbox.util.FileSystemManager.FileSystemEvent;

public class test  {
     public static void main(String[] args) {
     	/*Document a = JSONReturn.INVALID_PROTOCOL();
     	System.out.println("the output is "+a.getString("message"));

		 Document b = JSONReturn.CONNCECTION_REFUSED();
		 ArrayList<Document> c = (ArrayList<Document>) b.get("peers");

		 System.out.println("the output is "+ (c.get(1)).getString("host"));


		 Document doc1 = new Document();
		 doc1.append("host", "localhost");
		 doc1.append("port", 1234);

		 String host = doc1.getString("host");
		 int port = doc1.getInteger("port");
		 String json1 = doc1.toJson();

		 Document doc2 = Document.parse(json1);*/

		 Document a = new Document();
				 a = JSONReturn.FILE_CREATE_REQUEST ("FILE_CREATE_RESPONSE", "1432", 2019, 1024,"dir");
		 System.out.println(a.toJson());
	 }

		// System.out.println("The port form json is : "+dj.getInteger("port"));
		// System.out.println("The host from json is :" + dj.getString("host"));


/* implements FileSystemObserver
    	FileSystemObserver observe = new test();

			FileSystemManager test;
			try {
				test = new FileSystemManager("/Users/lilinyun/test", observe);
				test.makeDirectory("test1");
				test.createFileLoader("1","abc",1024,System.currentTimeMillis());
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


     }

	@Override
	public void processFileSystemEvent(FileSystemEvent fileSystemEvent) {
		// TODO Auto-generated method stub

	}*/

}
