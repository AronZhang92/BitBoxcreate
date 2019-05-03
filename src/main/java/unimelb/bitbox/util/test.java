package unimelb.bitbox.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import unimelb.bitbox.Connectionlist;
import unimelb.bitbox.util.FileSystemManager.FileSystemEvent;

public class test implements FileSystemObserver {
    private static Logger log = Logger.getLogger(test.class.getName());
    static protected FileSystemManager fileSystemManager;
    public test() throws NumberFormatException, IOException, NoSuchAlgorithmException {
        fileSystemManager=new FileSystemManager(Configuration.getConfigurationValue("path"),this);
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        FileSystemObserver a = new test();
        FileSystemManager fsm = new FileSystemManager("share", a); // should be replaced when generating
        if(fsm.dirNameExists("untitled folder 2")){
            System.out.println("The file name exists");
        }

    }

	@Override
	public void processFileSystemEvent(FileSystemEvent fileSystemEvent) {
		// TODO Auto-generated method stub
        System.out.println(fileSystemEvent);
        FileSystemManager.FileDescriptor fd = fileSystemEvent.fileDescriptor;
        Document doc = new Document();
        Document doc1 = new Document();
        byte[] b = null;
        try {
            b = fileSystemManager.readFile(fd.md5,0L,fd.fileSize).array();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] BiteStream = Base64.getEncoder().encode(b);
        String bite = Base64.getEncoder().encodeToString(BiteStream);
        if (fd != null) {
             doc1 = fd.toDoc();
        }
        doc.append("fileDescriptor",doc1);
        doc.append("content",bite);
        doc.append("pathName",fileSystemEvent.pathName);
        doc.append("path",fileSystemEvent.path);
        doc.append("name",fileSystemEvent.name);
        doc.append("event",fileSystemEvent.event.toString() + "_REQUEST");
        System.out.println(doc.toJson());
        switch (doc.getString("event")){
            case "DIRECTORY_DELETE_REQUEST" :
                System.out.println("System delete a directory");
                break;
            case "DIRECTORY_CREATE_REQUEST" :
                System.out.println("System create a directory");
                break;
        }


	}

}
