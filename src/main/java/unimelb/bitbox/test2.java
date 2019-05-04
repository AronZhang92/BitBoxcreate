package unimelb.bitbox;

import unimelb.bitbox.util.Configuration;

public class test2 {
   public static void main(String[] args) {
       Long starttime = System.currentTimeMillis();
	   System.out.println(starttime);
	   int i = 0;
	   while (i<1000000){
	       i++;
       }
	   Long endtime = System.currentTimeMillis();

       System.out.println("the runtime is" + (endtime - starttime));
   }
}
