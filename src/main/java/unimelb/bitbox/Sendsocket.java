package unimelb.bitbox;

import unimelb.bitbox.util.Document;
import unimelb.bitbox.util.FileSystemManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

public class Sendsocket {

    private static Logger log = Logger.getLogger(Sendsocket.class.getName());
    public static Document sendsocket(FileSystemManager.FileSystemEvent fileSystemEvent){

                FileSystemManager.FileDescriptor fd = fileSystemEvent.fileDescriptor;
                Document doc = new Document();
                Document do1 = new Document();

                if (fd != null) {
                    do1 = fd.toDoc();
                    doc.append("fileDescriptor",do1);
                }

                doc.append("pathName",fileSystemEvent.pathName);
                doc.append("command",fileSystemEvent.event.toString()+"_REQUEST");
                return doc;


    }
    public static void sendtosocket(Document doc,Socket socket){

        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            out.write(doc.toJson()+"\n");
            out.flush();
        } catch (InterruptedIOException e){
           log.info("The IO operation is interrupted, try again");
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
                out.write(doc.toJson()+"\n");
                out.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } catch (SocketException e){
            log.info("The connect is disconnect");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void sendDoc(Document doc){
        for (Socket socket : Connectionlist.returnsocketlist()
        ) {
                sendtosocket(doc,socket);
        }
    }
}
