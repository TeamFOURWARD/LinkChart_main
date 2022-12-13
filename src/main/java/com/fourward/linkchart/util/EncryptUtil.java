package com.fourward.linkchart.util;

import org.apache.tomcat.util.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

public class EncryptUtil {
    final static String salt = "djflseeejj";
    final static byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    final static String key = "3j4ijdfjoej04324";

    public static String encHashSHA256(String str) throws Exception {
        final String plaintText = salt + str;

        MessageDigest sh = MessageDigest.getInstance("SHA-256");
        sh.update(plaintText.getBytes());
        byte[] byteData = sh.digest();

        StringBuilder sb = new StringBuilder();
        for (byte byteDatum : byteData) {
            sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static String encAES128CBC(String str) throws Exception {
        byte[] textBytes = str.getBytes("UTF-8");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);

        return Base64.encodeBase64String(cipher.doFinal(textBytes));
    }

    public static String decAES128CBC(String str) throws Exception {
        byte[] textBytes = Base64.decodeBase64(str);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);

        return new String(cipher.doFinal(textBytes), "UTF-8");
    }
}
