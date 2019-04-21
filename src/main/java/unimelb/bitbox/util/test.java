package unimelb.bitbox.util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import unimelb.bitbox.util.FileSystemManager.FileSystemEvent;

public class test implements FileSystemObserver {
     public static void main(String[] args) {
    	 FileSystemObserver observe = new test();
    
			FileSystemManager test;
			try {
				test = new FileSystemManager("D:\\test", observe);
				test.makeDirectory("test1");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

     }

	@Override
	public void processFileSystemEvent(FileSystemEvent fileSystemEvent) {
		// TODO Auto-generated method stub
		
	}

}
