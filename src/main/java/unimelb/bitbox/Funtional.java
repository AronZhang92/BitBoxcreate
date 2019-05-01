package unimelb.bitbox;

import unimelb.bitbox.util.Document;
import unimelb.bitbox.util.FileSystemManager;
import unimelb.bitbox.util.FileSystemObserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;

public class Funtional {
	private static FileSystemManager.FileDescriptor fd;

	public static void funtional(Document doc) throws IOException, NoSuchAlgorithmException {

		FileSystemObserver ob = new ServerMain();
		FileSystemManager fsm = new FileSystemManager("share", ob); // should be replaced when generating

		switch (doc.getString("event")) {
		case "FILE_CREATE":
			if (fsm.isSafePathName(doc.getString("pathName"))) { // check if the pathname is safe
				if (!fsm.fileNameExists(doc.getString("pathName"))) { // when the file name doesn't exist
					fsm.createFileLoader(doc.getString("pathName"), doc.getString("md5"),     //create file loader
							Long.parseLong(doc.getString("fileSize")), Long.parseLong(doc.getString("lastModified")));
					if(fsm.checkShortcut(doc.getString("pathName"))) {
						break;      // stop when there is a shortcut
					}else {     // when there is no shortcut 
						
					}
				}
			} else

				break;
		case "FILE_DELETE":
			fsm.deleteFile(doc.getString("pathName"), doc.getLong("lastModicied"), doc.getString("md5"));
			break;
		case "FILE_MODIFY":
			break;
		case "DIRECTORY_CREATE":
			fsm.makeDirectory(doc.getString("pathName"));
			break;
		case "DIRECTORY_DELETE":
			fsm.deleteDirectory(doc.getString("pathName"));
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
