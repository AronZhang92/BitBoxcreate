package unimelb.bitbox;

import unimelb.bitbox.util.*;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class function2 {
    private static FileSystemManager.FileDescriptor fd;
    private static final Long blocksize = Long.parseLong(Configuration.getConfigurationValue("blockSize"));
    public static void funtional(Document doc,Socket socket) throws IOException, NoSuchAlgorithmException {

        FileSystemObserver ob = Peer.getServerMain();
        FileSystemManager fsm = new FileSystemManager("share", ob); // should be replaced when generating
        System.out.println(doc.toJson());
        Document fileDescriper = (Document) doc.get("fileDescriptor");
        switch (doc.getString("command")) {
            case "FILE_CREATE_REQUEST":
                if (fsm.isSafePathName(doc.getString("pathName"))) { // check if the pathname is safe
                    System.out.println("is sage pathname");
                    if (!fsm.fileNameExists(doc.getString("pathName"))) { // when the file name doesn't exist
                        System.out.println("The name is not exist");
                        fsm.createFileLoader(doc.getString("pathName"), fileDescriper.getString("md5"), // create file

                                // loader
                                fileDescriper.getLong("fileSize"),
                                fileDescriper.getLong("lastModified"));
                        System.out.println("Successful create file loaser");
                        Sendsocket.sendtosocket(JSONRETURN.FILE_CREATE_RESPONSE(fileDescriper, doc.getString("pathName"),
                                "file loader ready ", true),socket);  // send response when success creating file loader

                        if (fsm.checkShortcut(doc.getString("pathName"))) {
                            break; // stop when there is a shortcut
                        }
                        else { // when there is no shortcut
                            Sendsocket.sendtosocket(JSONRETURN2.FILE_BYTES_REQUEST(fileDescriper, doc.getString("pathName"), Long.parseLong("0"),blocksize),socket);



                        }
                    }else { // when file already exist
                        Sendsocket.sendDoc(JSONRETURN.FILE_CREATE_RESPONSE(fileDescriper, doc.getString("pathName"),
                                "file name already exist ", false));
                    }
                } else {
                    Sendsocket.sendDoc(JSONRETURN.FILE_CREATE_RESPONSE(fileDescriper, doc.getString("pathName"),
                            "pathName not safe ", false));
                }

                break;
            case "FILE_BYTES_RESQUEST":
                Long blocklength = doc.getLong("length");
                Long start = doc.getLong("position");
                Long filesize = fileDescriper.getLong("fileSize");

                    if (start + blocklength < filesize) {
                        byte[] b= new byte[fsm.readFile(fileDescriper.getString("md5"), start, start+blocklength).remaining()];
                        byte[] BiteStream = Base64.getEncoder().encode(b);
                        String bite = Base64.getEncoder().encodeToString(BiteStream);
                        Sendsocket.sendtosocket(JSONRETURN2.FILE_BYTES_RESPONCE(fileDescriper, doc.getString("pathName"),bite, "read successful", true, start+blocklength,blocklength),socket);
                    } else {
                        byte[] b= new byte[fsm.readFile(fileDescriper.getString("md5"), start, filesize).remaining()];
                        byte[] BiteStream = Base64.getEncoder().encode(b);
                        String bite = Base64.getEncoder().encodeToString(BiteStream);
                        Sendsocket.sendtosocket(JSONRETURN2.FILE_BYTES_RESPONCE(fileDescriper, doc.getString("pathName"),bite,"read successful", true, filesize,blocklength),socket);
                    }

                break;
            case "FILE_BYTES_RESPONSE":
                if(!fsm.checkWriteComplete(doc.getString("pathName"))) {
                    Long blocklength1 = doc.getLong("length");
                    Long start1 = doc.getLong("position");
                    Long filesize1 = fileDescriper.getLong("fileSize");

                    byte[] bites = Base64.getDecoder().decode(doc.getString("content"));
                    fsm.writeFile(doc.getString("pathName"), ByteBuffer.wrap(bites), start1);
                    if (start1 < filesize1) {
                        Sendsocket.sendtosocket(JSONRETURN2.FILE_BYTES_REQUEST(fileDescriper, doc.getString("pathName"), start1, blocklength1), socket);
                    }
                }
                break;

            case "FILE_CREATE_RESPONSE":

                break;

            case "FILE_DELETE":
                fsm.deleteFile(doc.getString("pathName"), doc.getLong("lastModicied"), doc.getString("md5"));
                break;
            case "FILE_MODIFY":
                break;
            case "DIRECTORY_CREATE_REQUEST":
                if (fsm.isSafePathName(doc.getString("pathName"))) { // check if the pathname is safe
                    if (!fsm.dirNameExists(doc.getString("pathName"))) { // when the directory name doesn't exist
                        fsm.makeDirectory(doc.getString("pathName"));
                    }
                }
                break;
            case "DIRECTORY_DELETE_REQUEST":

                if (fsm.isSafePathName(doc.getString("pathName"))) { // check if the pathname is safe

                    if (fsm.dirNameExists(doc.getString("pathName"))) { // when the directory name exist

                        fsm.deleteDirectory(doc.getString("pathName"));

                    }
                }
                break;
            default:
                break;
        }

    }

    private static void createfile(FileSystemManager.FileSystemEvent event, FileSystemManager fsm, ByteBuffer bb,
                                   long position) throws IOException, NoSuchAlgorithmException {
        String path = event.path;
        fd = event.fileDescriptor;
        fsm.createFileLoader(event.pathName, fd.md5, fd.fileSize, fd.lastModified);
        fsm.writeFile(event.pathName, bb, position);

    }
}
