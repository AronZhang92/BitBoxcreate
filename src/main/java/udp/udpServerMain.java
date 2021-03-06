package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Logger;

import unimelb.bitbox.Connectionlist;
import unimelb.bitbox.util.Configuration;
import unimelb.bitbox.util.Document;
import unimelb.bitbox.util.FileSystemManager;
import unimelb.bitbox.util.FileSystemObserver;
import unimelb.bitbox.util.JSONRETURN2;
import unimelb.bitbox.util.FileSystemManager.FileSystemEvent;

public class udpServerMain implements FileSystemObserver, Runnable {
	private static Logger log = Logger.getLogger(udpServerMain.class.getName());

//	static String portstring = Configuration.getConfigurationValue("udpPort");
//	static final int port = Integer.parseInt(portstring);
	protected static FileSystemManager fileSystemManager;
	private String maximumconnection = Configuration.getConfigurationValue("maximumIncommingConnections");
	private int maxcon = Integer.parseInt(maximumconnection);
	private DatagramSocket socket;

	public udpServerMain() throws NumberFormatException, IOException, NoSuchAlgorithmException {
		fileSystemManager = new FileSystemManager(Configuration.getConfigurationValue("path"), this);
	}

	public static FileSystemManager returnfilesm() {
		return fileSystemManager;
	}

	@Override
	public void processFileSystemEvent(FileSystemEvent fileSystemEvent) {
		byte[] msg = udpSendSocket.doctoByte(udpSendSocket.eventToDoc(fileSystemEvent)); // changing
		try {
			udpSendSocket.sendToAllPeers(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() { // wait for change

		while (true) {
			try {
				socket = udpPeer.getDatagramSocket();
				while (true) {
					DatagramPacket request = new DatagramPacket(new byte[13000], 13000);
					socket.receive(request);
					String msg = new String(request.getData(), request.getOffset(), request.getLength());
					Document doc = Document.parse(msg);

					int n = threadList.info.size();
					for (int j = 0; j < n; j++) {
					}

					threadList.removePacket(request, doc);
					int m = threadList.info.size();
					for (int j = 0; j < m; j++) {
					}

					// handshake
					if (doc.getString("command").equals("HANDSHAKE_REQUEST")) {
						if (udpConnectionList.getsize() < maxcon) { // when haven't reach mximum connection number
							log.info("received request from : " + request.getAddress());
							// send response
							Document responseDoc = JSONRETURN2.HANDSHAKE_RESPONSE(
									InetAddress.getLocalHost().getHostAddress(), socket.getLocalPort());
							byte[] responseByte = udpSendSocket.doctoByte(responseDoc);
							DatagramPacket response = new DatagramPacket(responseByte, responseByte.length,
									request.getAddress(), request.getPort());
							socket.send(response);
							// check if the peer already in the list
							if (udpConnectionList.contain(request.getAddress().toString())) {

							} else { // add the Datagramsocket to list, syncronize
								udpConnectionList.addudp(request.getAddress().toString().replace("/", ""),
										request.getPort());
								udpSynEvents.synevent(request.getAddress(), request.getPort());
							}
						} else { // when maximun connection number reached
							Document refuseRes = JSONRETURN2.CONNCECTION_REFUSED();
							byte[] refuseByte = udpSendSocket.doctoByte(refuseRes);
							DatagramPacket data = new DatagramPacket(refuseByte, refuseByte.length,
									request.getAddress(), request.getPort());
							socket.send(data);
						}

					} else { // when the command is not handshake_request
						String ipAdress = request.getAddress().toString().replace("/", "");
						udpFunction.funtional(doc, InetAddress.getByName(ipAdress), request.getPort());
					}
				}

			} catch (NumberFormatException | SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
