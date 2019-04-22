package unimelb.bitbox.util;
import java.security.SecureRandom;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;


public class DES {
    public void DES() throws Exception{
        String plainText = "Hello , world !";
        String key = "12345678";    //要求key至少长度为8个字符

        SecureRandom random = new SecureRandom();
        DESKeySpec keySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("des");
        SecretKey secretKey = keyFactory.generateSecret(keySpec);

        Cipher cipher = Cipher.getInstance("des");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
        byte[] cipherData = cipher.doFinal(plainText.getBytes());
        System.out.println("cipherText : " + new /*BASE64Encoder().*/String(cipherData));
        //PtRYi3sp7TOR69UrKEIicA==

        cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
        byte[] plainData = cipher.doFinal(cipherData);
        System.out.println("plainText : " + new String(plainData));
        //Hello , world !
    }



}
