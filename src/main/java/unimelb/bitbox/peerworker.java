package unimelb.bitbox;

import unimelb.bitbox.util.Configuration;
import unimelb.bitbox.util.Document;
import unimelb.bitbox.util.JSONRETURN2;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Logger;


public class peerworker implements Runnable {
    private static Logger log = Logger.getLogger(peerworker.class.getName());
    private Socket socket;
    final private String maximumconnection = Configuration.getConfigurationValue("maximumIncommingConnections");
    final private int maxcon = Integer.parseInt(maximumconnection);

    public peerworker(Socket socket) {
        this.socket = socket;
    }

    String clientMsg;

    public void run() {
        // TODO Auto-generated method stub
        if (Connectionlist.connum() < maxcon) {
            try {
                String host = Configuration.getConfigurationValue("advertisedName");
                String portString = Configuration.getConfigurationValue("port");
                int port = Integer.parseInt(portString);
                // Create a stream socket bounded to any port and connect it to the
                // socket bound to localhost on port 4444
                // Get the input/output streams for reading/writing data from/to the socket
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
                Document json = JSONRETURN2.HANDSHAKE_REQUEST(host,port);
                // Three way handshake
                out.write(json.toJson() + "\n");
                out.flush();
                System.out.println("client send a request :" + json.getString("command") + json.toJson());
                Document ack = new Document();
                String frombuffer = in.readLine();
                ack = Document.parse(frombuffer);
                System.out.println(ack.toJson());
                if (ack.getString("command").equals(JSONRETURN2
                        .HANDSHAKE_RESPONSE(socket.getInetAddress().toString(), socket.getPort()).getString("command"))) {
                    // System.out.println("received OK");
                    log.info("Connection established to" + socket.getInetAddress());
                    Connectionlist.addNewSocket(socket);
                    synevents.synevent(socket);
                    log.info("Successful add ip " + socket.getInetAddress() + " and port: " + socket.getPort()
                            + "to the connection list");

                    while ((clientMsg = in.readLine()) != null && Connectionlist.containsocket(socket)) { //deal with recerived commands
                        try {
                            function2.funtional(Document.parse(clientMsg), socket); //send jason object to class funtional
                        } catch (NoSuchAlgorithmException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                } // when the connection is refused
                else if (ack.getString("command").equals(JSONRETURN2.CONNCECTION_REFUSED().getString("command"))) {
                    ArrayList<Document> address = (ArrayList<Document>) ack.get("peers");
                    try {
                        if (address != null) {
                            AnotherConnection.AnotherConnection(address);
                        } else {
                            log.info("The socket closed due to wrong answer :" + frombuffer);
                            socket.close();
                            Connectionlist.remove(socket);
                        }
                    } catch (UnknownHostException e) {
                        log.info("unkown host try another one");
                    } catch (SocketException e){
                        log.info("Sock lost due to the other peer offline");

                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        // Close the socket
                        if (socket != null) {
                            try {
                                socket.close();
                                Connectionlist.remove(socket);
                            } catch (IOException e) {
                                log.info(" Cann't close the socket due to the IO exception");
                                Connectionlist.remove(socket);
                            }
                        }
                    }

                }
            } catch(NoSuchAlgorithmException e){
                e.printStackTrace();
            } catch(UnsupportedEncodingException e){
                Sendsocket.sendtosocket(JSONRETURN2.INVALID_PROTOCOL(),socket);
            } catch (SocketException e){
                log.info("Connection disconnect");
                log.info("The socket might be closed, trying to reconnection");
               ClientMain.reconnection(socket.getInetAddress().toString(),socket.getPort());
                try {
                    socket.close();
                    Connectionlist.remove(socket);
                }
                catch (UnknownHostException ue){
                    log.info("the host is unknow might cause by the wrong type or the discorrect ipadress ");
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
