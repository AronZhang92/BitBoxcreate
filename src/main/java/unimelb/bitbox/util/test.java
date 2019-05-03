package unimelb.bitbox.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import unimelb.bitbox.Connectionlist;
import unimelb.bitbox.util.FileSystemManager.FileSystemEvent;

public class test implements FileSystemObserver {
    private static Logger log = Logger.getLogger(test.class.getName());
    protected FileSystemManager fileSystemManager;
    public test() throws NumberFormatException, IOException, NoSuchAlgorithmException {
        fileSystemManager=new FileSystemManager(Configuration.getConfigurationValue("path"),this);
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        FileSystemObserver a = new test();

    }

	@Override
	public void processFileSystemEvent(FileSystemEvent fileSystemEvent) {
		// TODO Auto-generated method stub
        System.out.println(fileSystemEvent);
        FileSystemManager.FileDescriptor fd = fileSystemEvent.fileDescriptor;
        Document doc = new Document();
        if (fd != null) {
             doc = fd.toDoc();
        }
        doc.append("pathName",fileSystemEvent.pathName);
        doc.append("path",fileSystemEvent.path);
        doc.append("name",fileSystemEvent.name);
        doc.append("event",fileSystemEvent.event.toString());
        System.out.println(doc.toJson());


	}

}
