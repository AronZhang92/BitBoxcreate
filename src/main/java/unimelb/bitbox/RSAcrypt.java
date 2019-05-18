package unimelb.bitbox;


import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.oracle.tools.packager.IOUtils;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;

public class RSAcrypt {
    private ByteSource supplier;

    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }


    public static byte[] decrypt(PrivateKey privateKey, byte[] message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(message);
    }

    public static byte[] encrypt(PublicKey publicKey, byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(encrypted);
    }
    public RSAcrypt(byte[] data) {
        this.supplier = ByteSource.wrap(data);
    }
    private static byte[] readLengthFirst(InputStream in) throws IOException {
        int byte1 = in.read();
        int byte2 = in.read();
        int byte3 = in.read();
        int byte4 = in.read();
       int length = (byte1 << 24) + (byte2 << 16) + (byte3 << 8) + (byte4 << 0);
        byte[] val = new byte[length];
        //System.out.println("The length is " + length + "");
        ByteStreams.readFully(in, val);
        return val;
    }


    public static RSAPublicKeySpec convertToRSAPublicKey(String key) {
        try {
            byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(key);
            InputStream stream = new ByteArrayInputStream(keyBytes);
            String maker = new String(readLengthFirst(stream));
            BigInteger publicExponent = new BigInteger(readLengthFirst(stream));
            BigInteger modulus = new BigInteger(readLengthFirst(stream));
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, publicExponent);
            return keySpec;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    public static RSAPrivateKeySpec convertToRSAPrivateKey(String path) {
        try {

            String privatekey = readLineByLineJava8(path);
            privatekey = privatekey.replaceAll("\\n","").replace("-----BEGIN OPENSSH PRIVATE KEY-----","").replaceAll("-----END OPENSSH PRIVATE KEY-----","");
            byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(privatekey);
            InputStream stream = new ByteArrayInputStream(keyBytes);
            BigInteger publicExponent = new BigInteger(readLengthFirst(stream));
            BigInteger modulus = new BigInteger(readLengthFirst(stream));
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, publicExponent);
            return keySpec;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    static RSAPrivateCrtKeySpec decodeRSAPrivatePKCS1(byte[] encoded) {
        ByteBuffer input = ByteBuffer.wrap(encoded);
        if (der(input, 0x30) != input.remaining()) throw new IllegalArgumentException("Excess data");
        if (!BigInteger.ZERO.equals(derint(input))) throw new IllegalArgumentException("Unsupported version");
        BigInteger n = derint(input);
        BigInteger e = derint(input);
        BigInteger d = derint(input);
        BigInteger p = derint(input);
        BigInteger q = derint(input);
        BigInteger ep = derint(input);
        BigInteger eq = derint(input);
        BigInteger c = derint(input);
        return new RSAPrivateCrtKeySpec(n, e, d, p, q, ep, eq, c);
    }
    private static BigInteger derint(ByteBuffer input) {
        int len = der(input, 0x02);
        byte[] value = new byte[len];
        input.get(value);
        return new BigInteger(+1, value);
    }

    private static int der(ByteBuffer input, int exp) {
        int tag = input.get() & 0xFF;
        if (tag != exp) throw new IllegalArgumentException("Unexpected tag");
        int n = input.get() & 0xFF;
        if (n < 128) return n;
        n &= 0x7F;
        if ((n < 1) || (n > 2)) throw new IllegalArgumentException("Invalid length");
        int len = 0;
        while (n-- > 0) {
            len <<= 8;
            len |= input.get() & 0xFF;
        }
        return len;
    }
    private static String readLineByLineJava8(String filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }

    public static PublicKey getPublicKey(String key) throws Exception {
        RSAPublicKeySpec keySpec = convertToRSAPublicKey(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String path) throws Exception {
        byte[] keyByte = Files.readAllBytes(Paths.get(path));
        String newlines = new String(keyByte);
        newlines = newlines.replaceAll("-----BEGIN RSA PRIVATE KEY-----","").replaceAll("-----END RSA PRIVATE KEY-----","").replaceAll("\\n","");
        keyByte = (new BASE64Decoder()).decodeBuffer(newlines);
        RSAPrivateCrtKeySpec keySpec = decodeRSAPrivatePKCS1(keyByte);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
}
