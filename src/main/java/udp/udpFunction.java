package udp;

import unimelb.bitbox.RSAcrypt;
import unimelb.bitbox.util.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class udpFunction {
	private static FileSystemManager.FileDescriptor fd;
	private static final Long blocksize = Long.parseLong(Configuration.getConfigurationValue("blockSize"));
	private static Logger log = Logger.getLogger(udpFunction.class.getName());
    private static SecretKey commenKey = null;
	
	public static void funtional(Document doc, InetAddress address, int port) throws IOException, NoSuchAlgorithmException, InterruptedException {

		FileSystemManager fsm = udpServerMain.returnfilesm(); // should be replaced when generating
		Document fileDescriper = (Document) doc.get("fileDescriptor");
		switch (doc.getString("command")) {
		case "FILE_CREATE_REQUEST":
			if (fsm.isSafePathName(doc.getString("pathName"))) { // check if the pathname is safe
				if (!fsm.fileNameExists(doc.getString("pathName"))) { // when the file name doesn't exist
					fsm.createFileLoader(doc.getString("pathName"), fileDescriper.getString("md5"), // create file

							// loader
							fileDescriper.getLong("fileSize"), fileDescriper.getLong("lastModified"));
					udpSendSocket.sendtosocket(udpSendSocket.doctoByte(JSONRETURN2.FILE_CREATE_RESPONSE(fileDescriper, doc.getString("pathName"),
							"file loader ready ", true, 0L)), address, port); // send response when success creating file loader

					if (fsm.checkShortcut(doc.getString("pathName"))) {
						// System.out.println("Already check the short cut");
						break; // stop when there is a shortcut
					} else { // when there is no shortcut
						if (blocksize > fileDescriper.getLong("fileSize")) {
							udpSendSocket.sendtosocket(udpSendSocket.doctoByte(JSONRETURN2.FILE_BYTES_REQUEST(fileDescriper,
									doc.getString("pathName"), 0L, fileDescriper.getLong("fileSize"))), address, port);
						} else {
							udpSendSocket.sendtosocket(udpSendSocket.doctoByte(JSONRETURN2.FILE_BYTES_REQUEST(fileDescriper,
									doc.getString("pathName"), 0L, blocksize)), address, port);
						}

					}
				} else { // when file already exist
					udpSendSocket.sendToAllPeers(udpSendSocket.doctoByte(JSONRETURN2.FILE_CREATE_RESPONSE(fileDescriper, doc.getString("pathName"),
							"file name already exist ", false, 0L)));
				}
			} else {
				udpSendSocket.sendToAllPeers(udpSendSocket.doctoByte(JSONRETURN2.FILE_CREATE_RESPONSE(fileDescriper, doc.getString("pathName"),
						"pathName not safe ", false, 0L)));
			}

			break;
		case "FILE_BYTES_REQUEST":

			Long blocklength = doc.getLong("length");
			Long start = doc.getLong("position");
			Long filesize = fileDescriper.getLong("fileSize");

			byte[] b = fsm.readFile(fileDescriper.getString("md5"), start, blocklength).array();
			String bite = Base64.getEncoder().encodeToString(b);

			if (start == filesize) {
				udpSendSocket.sendtosocket(udpSendSocket.doctoByte(JSONRETURN2.FILE_BYTES_RESPONSE(fileDescriper, doc.getString("pathName"), bite,
						"read successful", true, start, filesize)), address, port);
			} else if (start + blocklength >= filesize) {
				udpSendSocket.sendtosocket(udpSendSocket.doctoByte(JSONRETURN2.FILE_BYTES_RESPONSE(fileDescriper, doc.getString("pathName"), bite,
						"read successful", true, start, filesize - start)), address, port);
			} else {
			//	TimeUnit.MILLISECONDS.sleep(500); //sleep for writing
				udpSendSocket.sendtosocket(udpSendSocket.doctoByte(JSONRETURN2.FILE_BYTES_RESPONSE(fileDescriper, doc.getString("pathName"), bite,
						"read successful", true, start, blocklength)), address, port);
			}

			break;
		case "FILE_BYTES_RESPONSE":

			Long blocklength1 = doc.getLong("length");
			Long start1 = doc.getLong("position");
			Long filesize1 = fileDescriper.getLong("fileSize");
			String content = doc.getString("content");

			if (content != null) {
				byte[] bites = Base64.getDecoder().decode(content);
				fsm.writeFile(doc.getString("pathName"), ByteBuffer.wrap(bites), start1);
			
			} else {
				log.info("System read nothing form the response");
			}
			if (start1 + blocklength1 == filesize1) {
				if (fsm.checkWriteComplete(doc.getString("pathName"))) {
					// System.out.println("Already check the complete and it is complete");
					fsm.cancelFileLoader(doc.getString("pathName"));
				} else {
					log.info(" failed to check complete");
				}

			} else if (start1 + blocklength1 + blocklength1 <= filesize1) {
//				TimeUnit.SECONDS.sleep(1);
				// remian still bigger or equal than the blocksize
				udpSendSocket.sendtosocket(udpSendSocket.doctoByte(JSONRETURN2.FILE_BYTES_REQUEST(fileDescriper, doc.getString("pathName"),
						start1 + blocklength1, blocklength1)), address, port);
			} else if (start1 + blocklength1 + blocklength1 > filesize1) {
//				TimeUnit.SECONDS.sleep(1);
				udpSendSocket.sendtosocket(udpSendSocket.doctoByte(JSONRETURN2.FILE_BYTES_REQUEST(fileDescriper, doc.getString("pathName"),
						start1 + blocklength1, filesize1 - start1 - blocklength1)), address, port);
			}

			break;

		case "FILE_CREATE_RESPONSE":
			log.info("Response of creating file " + doc.getString("pathName") + " is: " + doc.getString("message")
					+ ", staus: " + doc.getBoolean("status"));

			break;

		case "FILE_DELETE_REQUEST":
			String pathName = doc.getString("pathName");
			if (fsm.isSafePathName(pathName)) { // check if the pathname is safe

				if (fsm.fileNameExists(pathName)) { // when the directory name exist

					fsm.deleteFile(pathName, fileDescriper.getLong("lastModified"), fileDescriper.getString("md5"));
					udpSendSocket.sendtosocket(
							udpSendSocket.doctoByte(JSONRETURN2.FILE_DELETE_RESPONSE(fileDescriper, pathName, "successful delete", true)),
							address, port);

				} else {
					udpSendSocket.sendtosocket(
							udpSendSocket.doctoByte(JSONRETURN2.FILE_DELETE_RESPONSE(fileDescriper, pathName, "pathname does not exist", false)),
							address, port);
				}
			}

			break;

		case "FILE_DELETE_RESPONSE":
			log.info("Response of deleting file " + doc.getString("pathName") + " is: " + doc.getString("message")
					+ ", staus: " + doc.getBoolean("status"));
			break;

		case "FILE_MODIFY_REQUEST":
			if (fsm.isSafePathName(doc.getString("pathName"))) { // check if the pathname is safe
				if (fsm.fileNameExists(doc.getString("pathName"))) { // when the file exist
					if (!fsm.fileNameExists(doc.getString("pathName"), fileDescriper.getString("md5"))) { // when the
																											// content
																											// different
						fsm.modifyFileLoader(doc.getString("pathName"), fileDescriper.getString("md5"), // modify the
																										// file
								// loader
								fileDescriper.getLong("lastModified"));
						udpSendSocket.sendtosocket(udpSendSocket.doctoByte(JSONRETURN2.FILE_MODIFY_RESPONSE(fileDescriper,
								doc.getString("pathName"), "file loader ready ", true)), address, port); // send response when
																									// success creating
																									// file loader

						if (fsm.checkShortcut(doc.getString("pathName"))) {
							break; // stop when there is a shortcut
						} else { // when there is no shortcut
							if (blocksize > fileDescriper.getLong("fileSize")) {
								udpSendSocket.sendtosocket(udpSendSocket.doctoByte(JSONRETURN2.FILE_BYTES_REQUEST(fileDescriper,
										doc.getString("pathName"), 0L, fileDescriper.getLong("fileSize"))), address, port);
							} else {
								udpSendSocket.sendtosocket(udpSendSocket.doctoByte(JSONRETURN2.FILE_BYTES_REQUEST(fileDescriper,
										doc.getString("pathName"), 0L, blocksize)), address, port);
							}

						}
					} else { // when file already exist
						udpSendSocket.sendToAllPeers(udpSendSocket.doctoByte(JSONRETURN2.FILE_MODIFY_RESPONSE(fileDescriper, doc.getString("pathName"),
								"the content of the file are same ", false)));
					}
				} else {
					udpSendSocket.sendToAllPeers(udpSendSocket.doctoByte(JSONRETURN2.FILE_MODIFY_RESPONSE(fileDescriper, doc.getString("pathName"),
							"the fileName doesn't exist ", false)));
				}
			} else {
				udpSendSocket.sendToAllPeers(udpSendSocket.doctoByte(JSONRETURN2.FILE_MODIFY_RESPONSE(fileDescriper, doc.getString("pathName"),
						"pathName not safe ", false)));
			}

			break;

		case "FILE_MODIFY_RESPONSE":
			log.info("Response of modifing file " + doc.getString("pathName") + " is: " + doc.getString("message")
					+ ", staus: " + doc.getBoolean("status"));
			break;

		case "DIRECTORY_CREATE_REQUEST":
			if (fsm.isSafePathName(doc.getString("pathName"))) { // check if the pathname is safe
				if (!fsm.dirNameExists(doc.getString("pathName"))) { // when the directory name doesn't exist
					fsm.makeDirectory(doc.getString("pathName"));
					udpSendSocket.sendToAllPeers(udpSendSocket.doctoByte(JSONRETURN2.DIRECTORY_CREATE_RESPONSE(doc.getString("pathName"),
							"Directory create successfully", true)));
				} else {
					udpSendSocket.sendToAllPeers(udpSendSocket.doctoByte(JSONRETURN2.DIRECTORY_CREATE_RESPONSE(doc.getString("pathName"),
							"pathName already exist", false))); // when the directory name exist
				}
			} else {
				udpSendSocket.sendToAllPeers(
						udpSendSocket.doctoByte(JSONRETURN2.DIRECTORY_CREATE_RESPONSE(doc.getString("pathName"), "pathName not Safe", false)));
			}
			break;

		case "DIRECTORY_CREATE_RESPONSE":
			log.info("Response of creating directory " + doc.getString("pathName") + " is: " + doc.getString("message")
					+ ", staus: " + doc.getBoolean("status"));
			break;

		case "DIRECTORY_DELETE_REQUEST":
			String pathName1 = doc.getString("pathName");
			if (fsm.isSafePathName(pathName1)) { // check if the pathname is safe
				if (fsm.dirNameExists(pathName1)) { // when the directory name exist
					fsm.deleteDirectory(pathName1);
					udpSendSocket.sendtosocket(udpSendSocket.doctoByte(JSONRETURN2.DIRECTORY_DELETE_RESPONSE(pathName1, "successful delete", true)),
							address, port);
				} else {
					udpSendSocket.sendtosocket(
							udpSendSocket.doctoByte(JSONRETURN2.DIRECTORY_DELETE_RESPONSE(pathName1, "pathname does not exist", false)), address, port);
				}
			} else {
				udpSendSocket.sendToAllPeers(
						udpSendSocket.doctoByte(JSONRETURN2.DIRECTORY_DELETE_RESPONSE(doc.getString("pathName"), "pathName not Safe", false)));
			}

			break;

		case "DIRECTORY_DELETE_RESPONSE":
			log.info("Response of deleting directory " + doc.getString("pathName") + " is: " + doc.getString("message")
					+ ", staus: " + doc.getBoolean("status"));
			break;
		case "INVALID_PROTOCOL":
			log.info("Received INVALID_PROTOCOL response " + doc.getString("message"));
			break;

		// For client security connection
		case "AUTH_REQUEST":
			// check if identity exist
			String[] identityList = Configuration.getConfigurationValue("authorized_keys").split(",");
			ArrayList<String> identities = new ArrayList<String>();
			ArrayList<String> publicKeys = new ArrayList<String>();
			for (String identity : identityList) {
				identities.add(identity.split(" ")[2]);
				publicKeys.add(identity.split(" ")[1]);
			}
			// when identity exist
			if (identities.contains(doc.getString("identity"))) {
				int index = identities.indexOf(doc.getString("identity"));
				// generate a new AES common key
				KeyGenerator keyGen = null;
				try {
					keyGen = KeyGenerator.getInstance("AES");
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				keyGen.init(128);
			    commenKey = keyGen.generateKey();
				// commen key to String
				String commenKeyToStr = Base64.getEncoder().encodeToString(commenKey.getEncoded());
				byte[] commenKeyToByte = commenKey.getEncoded();
				// change public key from String to key
				try {
					PublicKey publicKey = RSAcrypt.getPublicKey(publicKeys.get(index)); // change String to public key
					byte[] enCommenKey = RSAcrypt.encrypt(publicKey, commenKeyToByte);
					String enCommenKeyToStr = Base64.getEncoder().encodeToString(enCommenKey);
					//send response to client
					udpSendSocket.sendtosocket(udpSendSocket.doctoByte(JSONRETURN2.AUTH_RESPONSE(enCommenKeyToStr, true, "public key found")),
							address, port);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				udpSendSocket.sendtosocket(udpSendSocket.doctoByte(JSONRETURN2.AUTH_RESPONSE( false, "public key not found")),
						address, port);
			}
			break;
		case "HANDSHAKE_RESPONSE":
			udpConnectionList.addudp(address.toString(), port);
			
		case "LIST_PEERS_REQUEST":
			// send connected peer list, wait to be implemented
			break;
			
		case "CONNECT_PEER_REQUEST":
			// connect a peer, wait to be implemented
			break;
			
		case "DISCONNECT_PEER_REQUEST":
		   // disconnect a peer, wait to be implemented
		   break;
		
		default:
			udpSendSocket.sendtosocket(udpSendSocket.doctoByte(JSONRETURN2.INVALID_PROTOCOL()), address, port);
			break;
		}

	}
    
	public static SecretKey getSecreteKey() {
		return commenKey;
	}
}
