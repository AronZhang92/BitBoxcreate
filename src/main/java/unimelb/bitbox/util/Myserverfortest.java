package unimelb.bitbox.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class Myserverfortest {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(8888);
        while (true) {
            DatagramPacket request = new DatagramPacket(new byte[200], 200);
            socket.receive(request);

            System.out.println(Arrays.toString(request.getData()));

            String quote = new String(request.getData(),request.getOffset(), request.getLength());
            System.out.println(request.getLength());
            System.out.println(quote);

            String send = "I am iron man AND I AM BATMAN";

            byte[] buffer = send.getBytes();

            InetAddress clientAddress = request.getAddress();
            int clientPort = request.getPort();
            System.out.println("the ip adress is " + clientAddress + " the client port is " + clientPort);
            DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
            socket.send(response);
        }

    }
}
