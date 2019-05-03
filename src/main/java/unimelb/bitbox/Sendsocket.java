package unimelb.bitbox;

import unimelb.bitbox.util.Document;
import unimelb.bitbox.util.FileSystemManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Sendsocket {
    public static Document sendsocket(FileSystemManager.FileSystemEvent fileSystemEvent){

                FileSystemManager.FileDescriptor fd = fileSystemEvent.fileDescriptor;
                Document doc = new Document();
                if (fd != null) {
                    doc = fd.toDoc();
                }
                doc.append("pathName",fileSystemEvent.pathName);
                doc.append("path",fileSystemEvent.path);
                doc.append("name",fileSystemEvent.name);
                doc.append("command",fileSystemEvent.event.toString()+"_REQUEST");
                return doc;


    }
    public static void sendDoc(Document doc){
        for (Socket socket: Connectionlist.returnsocketlist()
        ) {
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
                out.write(doc.toJson()+"\n");
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
