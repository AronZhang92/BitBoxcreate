package udp;

import unimelb.bitbox.util.Document;

import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class test {

	public static void main(Document doc) throws InterruptedException {
		String[] a = {"id","command"};
		System.out.println(a[1].equals("haf"));
		Base64.getDecoder().decode(doc.toJson());
	}

	public static void main(String[] args) {
		String abc = "{\"hostPort\":{\"port\":6667,\"host\":\"10.13.187.139\"},\"command\":\"HANDSHAKE_REQUEST\"}";
		abc = abc.replace("\r\n","");
		System.out.println(abc);
		Base64.getDecoder().decode(abc);
	}

}
