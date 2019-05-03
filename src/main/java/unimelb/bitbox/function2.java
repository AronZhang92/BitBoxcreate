package unimelb.bitbox;

import unimelb.bitbox.util.*;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class function2 {
    private static FileSystemManager.FileDescriptor fd;
    private static final Long blocksize = Long.parseLong(Configuration.getConfigurationValue("blockSize"));
    public static void funtional(Document doc,Socket socket) throws IOException, NoSuchAlgorithmException {

        FileSystemManager fsm = ServerMain.returnfilesm(); // should be replaced when generating
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
                        Sendsocket.sendtosocket(JSONRETURN2.FILE_CREATE_RESPONSE(fileDescriper, doc.getString("pathName"),
                                "file loader ready ", true,0L),socket);  // send response when success creating file loader

                        if (fsm.checkShortcut(doc.getString("pathName"))) {
                            System.out.println("Already check the short cut");
                            break; // stop when there is a shortcut
                        }
                        else { // when there is no shortcut
                            Long position1 = 0L;
                            Sendsocket.sendtosocket(JSONRETURN2.FILE_BYTES_REQUEST(fileDescriper, doc.getString("pathName"), position1,blocksize),socket);
                            System.out.println("Call to wait read the file");


                        }
                    }else { // when file already exist
                        Sendsocket.sendDoc(JSONRETURN2.FILE_CREATE_RESPONSE(fileDescriper, doc.getString("pathName"),
                                "file name already exist ", false,0L));
                    }
                } else {
                    Sendsocket.sendDoc(JSONRETURN2.FILE_CREATE_RESPONSE(fileDescriper, doc.getString("pathName"),
                            "pathName not safe ", false,0L));
                }

                break;
            case "FILE_BYTES_RESQUEST":

                Long blocklength = doc.getLong("length");
                Long start = doc.getLong("position");
                Long filesize = fileDescriper.getLong("fileSize");

                System.out.println("in FILE_BYTES_REQUEST Get the length " + blocklength + " position " + start + " filesize " + filesize);

                    if (start + blocklength < filesize) {
                        byte[] b= fsm.readFile(fileDescriper.getString("md5"), start, blocklength-start).array();
                        byte[] BiteStream = Base64.getEncoder().encode(b);
                        String bite = Base64.getEncoder().encodeToString(BiteStream);
                        Sendsocket.sendtosocket(JSONRETURN2.FILE_BYTES_RESPONCE(fileDescriper, doc.getString("pathName"),bite, "read successful", true, start,blocklength),socket);
                    } else {
                        //

                        byte[] b= fsm.readFile(fileDescriper.getString("md5"), start, filesize-start).array();
                        byte[] BiteStream = Base64.getEncoder().encode(b);
                        String bite = Base64.getEncoder().encodeToString(BiteStream);
                        System.out.println("The byte we write the " + bite + "in the FILE_BYTES_RESPOND");
                        Sendsocket.sendtosocket(JSONRETURN2.FILE_BYTES_RESPONCE(fileDescriper, doc.getString("pathName"),bite,"read successful", true, start,blocklength),socket);
                        System.out.println("the filesize is " + filesize +"  blocklength "+blocklength + " in the FILE_BYTES_RESPOND");
                    }

                break;
            case "FILE_BYTES_RESPONSE":
                if(!fsm.checkWriteComplete(doc.getString("pathName"))) {
                    Long blocklength1 = doc.getLong("length");
                    Long start1 = doc.getLong("position");
                    Long filesize1 = fileDescriper.getLong("fileSize");

                    System.out.println(" In the FILE_BYTES_RESPONSE Get the length " + blocklength1 + " position " + start1 + " filesize " + filesize1);
                    String content = doc.getString("content");
                    if (content != null){
                        byte[] bites = Base64.getDecoder().decode(content);
                        fsm.writeFile(doc.getString("pathName"), ByteBuffer.wrap(bites), start1);
                    } else {
                        System.out.println("System read nothing form the file");
                    }
                    if (start1 < filesize1) {
                        Sendsocket.sendtosocket(JSONRETURN2.FILE_BYTES_REQUEST(fileDescriper, doc.getString("pathName"), start1 + blocklength1, blocklength1), socket);
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
