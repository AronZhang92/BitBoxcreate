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
        FileSystemManager fsm = new FileSystemManager("share",a);
        try {

            ByteBuffer bb = fsm.readFile("25ba4d9c9ef0e875455e547e8a4407a7",0,202);
            fsm.createFileLoader("ANC.rtf","6b7522ee4b397328dcb41e4317f13b6b",202,1556625084);
            fsm.writeFile("ANC.rtf",bb,0);
            //while(!fsm.checkWriteComplete("ANC.rtg"));
            fsm.checkShortcut("ANC.rtf");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void processFileSystemEvent(FileSystemEvent fileSystemEvent) {
		// TODO Auto-generated method stub
        System.out.println(fileSystemEvent);
        /*FileSystemManager.FileDescriptor fd = fileSystemEvent.fileDescriptor;
        Document doc = new Document();
        if (fd != null) {
             doc = fd.toDoc();
        }
        doc.append("pathName",fileSystemEvent.pathName);
        doc.append("path",fileSystemEvent.path);
        doc.append("name",fileSystemEvent.name);
        doc.append("event",fileSystemEvent.event.toString());
        System.out.println(doc.toJson());*/


	}

}
