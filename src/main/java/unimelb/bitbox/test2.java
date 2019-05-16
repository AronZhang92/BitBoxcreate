package unimelb.bitbox;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import unimelb.bitbox.util.Configuration;

public class test2 {
   public static void main(String[] args) {
	   String publick = Configuration.getConfigurationValue("publickey");
	   System.out.println(Configuration.getConfigurationValue("publickey"));
	   byte[] publicBytes = Base64.getDecoder().decode(publick);
	   KeyFactory keyFactory = null;
	try {
		keyFactory = KeyFactory.getInstance("RSA");
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   X509EncodedKeySpec speckey = new X509EncodedKeySpec(publicBytes);
	   try {
		PublicKey pubKey = keyFactory.generatePublic(speckey);
	} catch (InvalidKeySpecException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
