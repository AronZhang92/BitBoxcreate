package unimelb.bitbox;

import unimelb.bitbox.util.Configuration;

public class test2 {
   public static void main(String[] args) {
	   System.out.println(Long.parseLong(Configuration.getConfigurationValue("blockSize")));
   }
}
