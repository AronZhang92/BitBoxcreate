package udp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import unimelb.bitbox.Peer;
import unimelb.bitbox.Sendsocket;
import unimelb.bitbox.util.FileSystemManager;
import unimelb.bitbox.util.FileSystemObserver;

public class udpSynEvents {

	public static void synevent(InetAddress address, int port) throws IOException, NoSuchAlgorithmException {
		FileSystemObserver ob = Peer.getServerMain();
		FileSystemManager fsm = udpServerMain.fileSystemManager; //
		ArrayList<FileSystemManager.FileSystemEvent> eventslist = fsm.generateSyncEvents();
		for (FileSystemManager.FileSystemEvent event : eventslist) {
			byte[] msg = udpSendSocket.doctoByte(udpSendSocket.eventToDoc(event));
			udpSendSocket.sendToPeer(msg, address, port);
		}
	}

	public static void syneventtoall() throws IOException, NoSuchAlgorithmException {
		ArrayList<String> peers = udpConnectionList.getall();
		if (peers.size() == 0) {
			FileSystemObserver ob = Peer.getServerMain();
			FileSystemManager fsm = udpServerMain.fileSystemManager;
			ArrayList<FileSystemManager.FileSystemEvent> eventslist = fsm.generateSyncEvents();

			for (FileSystemManager.FileSystemEvent event : eventslist) {
            byte[] data=udpSendSocket.doctoByte(udpSendSocket.eventToDoc(event));
            udpSendSocket.sendToAllPeers(data);
			}

		}
	}

}
