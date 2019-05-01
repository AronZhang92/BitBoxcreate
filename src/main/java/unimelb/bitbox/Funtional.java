package unimelb.bitbox;

import unimelb.bitbox.util.Document;
import unimelb.bitbox.util.FileSystemManager;
import unimelb.bitbox.util.FileSystemObserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;

public class Funtional {
    private static FileSystemManager.FileDescriptor fd;


     public static void funtional(Document doc)throws IOException, NoSuchAlgorithmException {

        FileSystemObserver ob = new ServerMain();
        FileSystemManager fsm = new FileSystemManager("share",ob);

        switch (doc.getString("event")){
            case "FILE_CREATE": break;
            case "FILE_DELETE": fsm.deleteFile(doc.getString("pathName"),doc.getLong("lastModicied"),doc.getString("md5"));break;
            case "FILE_MODIFY": break;
            case "DIRECTORY_CREATE": fsm.makeDirectory(doc.getString("pathName"));break;
            case "DIRECTORY_DELETE": fsm.deleteDirectory(doc.getString("pathName"));break;
            default:break;
        }

    }

    private static void createfile(FileSystemManager.FileSystemEvent event, FileSystemManager fsm, ByteBuffer bb, long position) throws IOException, NoSuchAlgorithmException {
         String path = event.path;
         fd = event.fileDescriptor;
         fsm.createFileLoader(event.pathName,fd.md5,fd.fileSize,fd.lastModified);
         fsm.writeFile(event.pathName,bb,position);


    }
}
