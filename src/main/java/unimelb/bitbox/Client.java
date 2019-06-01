package unimelb.bitbox;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Logger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.plaf.synth.SynthOptionPaneUI;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import unimelb.bitbox.AEScrypt;
import unimelb.bitbox.CmdLineArgs;
import unimelb.bitbox.Connectionlist;
import unimelb.bitbox.RSAcrypt;
import unimelb.bitbox.ServerWorker;
import unimelb.bitbox.function2;
import unimelb.bitbox.util.Configuration;
import unimelb.bitbox.util.Document;
import unimelb.bitbox.util.JSONRETURN2;

public class Client {
	public static void main(String[] args) {
		Logger log = Logger.getLogger(Client.class.getName());
		CmdLineArgs cmd = new CmdLineArgs();
    	CmdLineParser parser = new CmdLineParser(cmd);
    	try {
    		parser.parseArgument(args);

     	} catch (CmdLineException e) {
    		e.printStackTrace();
     	}
    	String server = cmd.getserver();
    	String command = cmd.getcommand();
    	String peer = cmd.getpeer();
    	Socket socket;
    	String[] middleserver = server.split(":");
    	String ipAddress1 = middleserver[0];
    	int port1 = Integer.parseInt(middleserver[1]);

    	try {
    		socket = new Socket(ipAddress1, port1);

    		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
    		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

    		out.write(JSONRETURN2.AUTH_REQUEST(cmd.getidentify()).toJson() + "\n");
    		out.flush();

    		String frombuffer = in.readLine();
    		Document doc = Document.parse(frombuffer);

    		byte[] keyCommon = null;
    		SecretKey key_common = null;
    		PrivateKey keyPrivate = null;
    		try {
    			keyPrivate = RSAcrypt.getPrivateKey("src/bitboxclient_rsa");
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
				  e.printStackTrace();
    		}

    		if(doc.getString("command").equals("AUTH_RESPONSE")) {
    			if (doc.getBoolean("status") == true) {
    				String AES128 = doc.getString("AES128");
    				try {
    					keyCommon = RSAcrypt.decrypt(keyPrivate, Base64.getDecoder().decode(AES128));
    					key_common = new SecretKeySpec(keyCommon, 0, keyCommon.length, "AES");
    					String commonkey = Base64.getEncoder().encodeToString(keyCommon);
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
						  e.printStackTrace();
    				}
    			}else if (doc.getBoolean("status") == false) {
    				log.info("public key not found");
    				System.exit(0);
    			}
    		}

    		Document document = new Document();
    		switch(command) {
    			case "list_peers":
    				try {
    					String list_peers = AEScrypt.encrypt
												(JSONRETURN2.LIST_PEERS_REQUEST().toJson(), key_common);
    					document.append("payload", list_peers);
    					out.write(document.toJson() + "\n");
						out.flush();

						String buffer1 = in.readLine();
						buffer1 = Document.parse(buffer1).getString("payload");

						String buffers = AEScrypt.decrypt(buffer1, key_common);
						Document doc1 = Document.parse(buffers);
						if(doc1.getString("command").equals("LIST_PEERS_RESPONSE")) {
							ArrayList<Document> a = (ArrayList<Document>) doc1.get("peers");
							ArrayList<Document> address = null;
							if (a.size() != 0) {
								address = (ArrayList<Document>) doc1.get("peers");
								if (address != null) {
									for (Document doc2 : address) {
										if (doc2 != null) {
											String ipAaddress3 = doc2.getString("host");
											long port3 = doc2.getLong("port");
											log.info("host :" + ipAaddress3 + "\n" + "port :" + port3 + "\n");
										}
									}
								}
							} else {
								log.info("no peer connect");
							}
						}
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
						  e.printStackTrace();
    				}
    				break;

    			case "connect_peer":
    				try {
						String[] middlepeer = peer.split(":");
						String ipAddress2 = middlepeer[0];
						int port2 = Integer.parseInt(middlepeer[1]);

						String connect_peer = AEScrypt.encrypt(JSONRETURN2.
													CONNECT_PEER_REQUEST(ipAddress2, port2).toJson(), key_common);
    					document.append("payload", connect_peer);
    					out.write(document.toJson() + "\n");
    					out.flush();

						String buffer2 = in.readLine();
						buffer2 = Document.parse(buffer2).getString("payload");
						String buffers = AEScrypt.decrypt(buffer2, key_common);
						Document doc2 = Document.parse(buffers);
			    		if (doc2.getString("command").equals("CONNECT_PEER_RESPONSE")) {
							boolean status2 = doc2.getBoolean("status");
							String message2 = doc2.getString("message");
							log.info("status : " + status2 + "\n" + "message : " +  message2) ;
			    		}
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
						  e.printStackTrace();
    				}
    				break;

	    		case "disconnect_peer":
	    			try {
						String[] middlepeer = peer.split(":");
						String ipAddress2 = middlepeer[0];
						int port2 = Integer.parseInt(middlepeer[1]);

						String disconnect_peer = AEScrypt.encrypt(JSONRETURN2.
													DISCONNECT_PEER_REQUEST(ipAddress2, port2).toJson(), key_common);
	    				document.append("payload", disconnect_peer);
	    				out.write(document.toJson() + "\n");
						out.flush();
				
						String buffer3 = in.readLine();
						buffer3 = Document.parse(buffer3).getString("payload");
						String buffers = AEScrypt.decrypt(buffer3, key_common);
						Document doc3 = Document.parse(buffers);
						if (doc3.getString("command").equals("DISCONNECT_PEER_RESPONSE")) {
							boolean status3 = doc3.getBoolean("status");
							String message3 = doc3.getString("message");
							log.info("status : " + status3 + "\n" + "message : " +  message3) ;
						}
	    			} catch (Exception e) {
	    				// TODO Auto-generated catch block
						  e.printStackTrace();
	    			}
	    			break;
    		}

    		in.close();
	    	out.close();
			socket.close();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
			  e.printStackTrace();
    	}
	}
}
