package udp;

import java.util.concurrent.TimeUnit;

public class test {

	public static void main(String[] args) throws InterruptedException {
		for(int i=1; i<100; i++) {
			System.out.println(i);
	}
		for(int i=1; i<100; i++) {
				TimeUnit.SECONDS.sleep(1);
				System.out.println(i);
		}
	
	}

}
