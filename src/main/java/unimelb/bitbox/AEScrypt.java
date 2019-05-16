package unimelb.bitbox;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.Key;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AEScrypt {

    private static Key generateKey(byte[] key) throws Exception {
        return new SecretKeySpec(key, "AES");
    }

    public static String encrypt(String data, Key key) throws Exception
    {
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return new BASE64Encoder().encode(encVal);
    }

    public static String decrypt(String encryptedData, Key key) throws Exception
    {
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }
    
}
