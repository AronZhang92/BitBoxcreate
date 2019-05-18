package unimelb.bitbox;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Stream;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import unimelb.bitbox.util.Configuration;

public class test2 {
   public static void main(String[] args) {
	   String publick = Configuration.getConfigurationValue("publickey");

       try {
           PublicKey pubKey = RSAcrypt.getPublicKey(publick);
           String message = "LILINYUN";
           System.out.println("Before encryt :" + message);
           byte[] encrypt = RSAcrypt.encrypt(pubKey,message.getBytes());
           String newcode = Base64.getEncoder().encodeToString(encrypt);
           System.out.println("After encrypt"+newcode);
           encrypt = Base64.getDecoder().decode(newcode);
           PrivateKey privateKey = RSAcrypt.getPrivateKey("id-rsa");
           byte [] decrypt = RSAcrypt.decrypt(privateKey,encrypt);
           System.out.println("decrypt :" +new String(decrypt));

       } catch (Exception e) {
           e.printStackTrace();
       }

   }


}
