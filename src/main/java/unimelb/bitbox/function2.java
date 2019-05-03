package unimelb.bitbox;

import unimelb.bitbox.util.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class function2 {
    private static FileSystemManager.FileDescriptor fd;
    private static final Long blocksize = Long.parseLong(Configuration.getConfigurationValue("blockSize"));
    public static void funtional(Document doc, Socket socket) throws IOException, NoSuchAlgorithmException {

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
                            if(blocksize > fileDescriper.getLong("fileSize")){
                                Sendsocket.sendtosocket(JSONRETURN2.FILE_BYTES_REQUEST(fileDescriper, doc.getString("pathName"), 0L,fileDescriper.getLong("fileSize")),socket);
                            } else {
                                Sendsocket.sendtosocket(JSONRETURN2.FILE_BYTES_REQUEST(fileDescriper, doc.getString("pathName"), 0L, blocksize), socket);
                            }
                            System.out.println("Call to wait read the file from " + socket.getInetAddress());


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
            case "FILE_BYTES_REQUEST":

                Long blocklength = doc.getLong("length");
                Long start = doc.getLong("position");
                Long filesize = fileDescriper.getLong("fileSize");

                System.out.println("in FILE_BYTES_REQUEST Get the length " + blocklength + " position " + start + " filesize " + filesize);

                        byte[] b= fsm.readFile(fileDescriper.getString("md5"), start, blocklength).array();
                        String bite = Base64.getEncoder().encodeToString(b);

                        if(start == filesize){
                            Sendsocket.sendtosocket(JSONRETURN2.FILE_BYTES_RESPONCE(fileDescriper, doc.getString("pathName"), bite, "read successful", true, start, filesize), socket);
                        }
                        else  if (start + blocklength >= filesize) {
                            Sendsocket.sendtosocket(JSONRETURN2.FILE_BYTES_RESPONCE(fileDescriper, doc.getString("pathName"), bite, "read successful", true, start, filesize-start), socket);
                        }
                        else {
                            Sendsocket.sendtosocket(JSONRETURN2.FILE_BYTES_RESPONCE(fileDescriper, doc.getString("pathName"), bite, "read successful", true, start, blocklength), socket);
                        }

                break;
            case "FILE_BYTES_RESPONSE":

                    Long blocklength1 = doc.getLong("length");
                    Long start1 = doc.getLong("position");
                    Long filesize1 = fileDescriper.getLong("fileSize");

                    System.out.println(" In the FILE_BYTES_RESPONSE Get the length " + blocklength1 + " position " + start1 + " filesize " + filesize1);
                    String content = doc.getString("content");
                    if (content != null){
                        byte[] bites = Base64.getDecoder().decode(content);
                        System.out.println(" the text reveived is :"+ByteBuffer.wrap(bites));
                        fsm.writeFile(doc.getString("pathName"), ByteBuffer.wrap(bites), start1);
                    } else {
                        System.out.println("System read nothing form the response");
                    }
                    if(start1 + blocklength1 == filesize1){
                        if(fsm.checkWriteComplete(doc.getString("pathName"))){
                            //System.out.println("Already check the complete and it is complete");
                            fsm.cancelFileLoader(doc.getString("pathName"));
                        }
                        else {
                            System.out.println(" failed to check complete");
                        }
                        System.out.println("now in the equal part: position equal fileSize");
                    }
                    else if (start1 + blocklength1 + blocklength1 < filesize1) {
                        Sendsocket.sendtosocket(JSONRETURN2.FILE_BYTES_REQUEST(fileDescriper, doc.getString("pathName"), start1 + blocklength1, blocklength1), socket);
                    } else if(start1 + blocklength1 + blocklength1> filesize1){
                        Sendsocket.sendtosocket(JSONRETURN2.FILE_BYTES_REQUEST(fileDescriper, doc.getString("pathName"), start1 + blocklength1, filesize1-start1-blocklength1), socket);
                    }
                    else {

                   }

                break;

            case "FILE_CREATE_RESPONSE":
                System.out.println("THis is a FILE_CREATE_RESPONSE");

                break;

            case "FILE_DELETE_REQUEST":
                String pathName = doc.getString("pathName");
                if (fsm.isSafePathName(pathName)) { // check if the pathname is safe

                    if (fsm.fileNameExists(pathName)) { // when the directory name exist

                        fsm.deleteFile(pathName,fileDescriper.getLong("lastModified"),fileDescriper.getString("md5"));
                        Sendsocket.sendtosocket(JSONRETURN2.FILE_DELETE_RESPONCE(fileDescriper,pathName,"successful delete",true),socket);

                    } else {
                        Sendsocket.sendtosocket(JSONRETURN2.FILE_DELETE_RESPONCE(fileDescriper,pathName,"pathname does not exist",false),socket);
                    }
                }

                break;
            case "FILE_MODIFY_REQUEST":
            	 if (fsm.isSafePathName(doc.getString("pathName"))) { // check if the pathname is safe
                     System.out.println("is sage pathname");
                     if (fsm.fileNameExists(doc.getString("pathName"))) { // when the file name exist
                         System.out.println("The name is not exist");
                         fsm.createFileLoader(doc.getString("pathName"), fileDescriper.getString("md5"), // create file

                                 // loader
                                 fileDescriper.getLong("fileSize"),
                                 fileDescriper.getLong("lastModified"));
                         System.out.println("Successful create file loaser");
                         Sendsocket.sendtosocket(JSONRETURN2.FILE_MODIFY_RESPONSE(fileDescriper, doc.getString("pathName"),
                                 "file loader ready ", true),socket);  // send response when success creating file loader

                         if (fsm.checkShortcut(doc.getString("pathName"))) {
                             System.out.println("Already check the short cut");
                             break; // stop when there is a shortcut
                         }
                         else { // when there is no shortcut
                             if(blocksize > fileDescriper.getLong("fileSize")){
                                 Sendsocket.sendtosocket(JSONRETURN2.FILE_BYTES_REQUEST(fileDescriper, doc.getString("pathName"), 0L,fileDescriper.getLong("fileSize")),socket);
                             } else {
                                 Sendsocket.sendtosocket(JSONRETURN2.FILE_BYTES_REQUEST(fileDescriper, doc.getString("pathName"), 0L, blocksize), socket);
                             }
                             System.out.println("Call to wait read the file from " + socket.getInetAddress());


                         }
                     }else { // when file already exist
                         Sendsocket.sendDoc(JSONRETURN2.FILE_MODIFY_RESPONSE(fileDescriper, doc.getString("pathName"),
                                 "file name doesn't exist ", false));
                     }
                 } else {
                     Sendsocket.sendDoc(JSONRETURN2.FILE_MODIFY_RESPONSE(fileDescriper, doc.getString("pathName"),
                             "pathName not safe ", false));
                 }

                 break;
                
                
            case "DIRECTORY_CREATE_REQUEST":
                if (fsm.isSafePathName(doc.getString("pathName"))) { // check if the pathname is safe
                    if (!fsm.dirNameExists(doc.getString("pathName"))) { // when the directory name doesn't exist
                        fsm.makeDirectory(doc.getString("pathName"));
                    }
                }
                break;
            case "DIRECTORY_DELETE_REQUEST":
                String pathName1 = doc.getString("pathName");
                if (fsm.isSafePathName(pathName1)) { // check if the pathname is safe
                    if (fsm.dirNameExists(pathName1)) { // when the directory name exist
                        fsm.deleteDirectory(pathName1);
                        Sendsocket.sendtosocket(JSONRETURN2.DIRECTORY_DELETE_RESPONCE(pathName1,"successful delete",true),socket);
                    } else {
                        Sendsocket.sendtosocket(JSONRETURN2.DIRECTORY_DELETE_RESPONCE(pathName1,"pathname does not exist",false),socket);
                    }
                }

                    break;
            default:
                System.out.println("Can not match any command" + doc.getString("command"));
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
