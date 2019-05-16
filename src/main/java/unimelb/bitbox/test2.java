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
	   String[] identityList = Configuration.getConfigurationValue("authorized_keys").split(",");
		ArrayList<String> identities = new ArrayList<String>();
		ArrayList<String> publicKeys = new ArrayList<String>();
		for (String identity : identityList) {
			identities.add(identity.split(" ")[2]);
			publicKeys.add(identity.split(" ")[1]);
		}
		try {
			System.out.println(publicKeys.get(0));
			RSAcrypt.getPublicKey(publicKeys.get(0));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
}
