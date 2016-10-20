package com.caoyang.tapon.Utils;


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密解密工具包
 */
public class CyptoUtils {

    private static AlgorithmParameterSpec spec = getIV();
    final public static String privateKey = "123456789123456789123456789123456789123456789123456789123456789123456789123456789";


    /**
     * 获取秘钥（SHA-256）
     *
     * @param strSrc
     * @return
     */
    private static byte[] generateKey(String strSrc) {

        MessageDigest digest;

        byte[] keyBytes = new byte[32];
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(strSrc.getBytes("UTF-8"));
            System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return keyBytes;
    }

    /**
     * AES加密
     *
     * @param content    需要加密的内容
     * @param privateKey 加密密码
     * @return
     */
    public static String encrypt(String content, String privateKey) {
        byte[] secretKey = generateKey(privateKey);
        return encrypt(content, secretKey);
    }

    private static String encrypt(String content, byte[] bts) {
        try {
            Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec key = new SecretKeySpec(bts, "AES");
            aesCBC.init(Cipher.ENCRYPT_MODE, key, spec);
            byte[] result = aesCBC.doFinal(content.getBytes());
            String str = new String(result, "UTF-8");
            String encryptedText = new String(Base64.encode(result, Base64.NO_WRAP), "UTF-8");
            return encryptedText;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * AES解密
     *
     * @param content    待解密内容
     * @param privateKey 解密密钥
     * @return
     */
    public static String decrypt(String content, String privateKey) {
        byte[] secretKey = generateKey(privateKey);
        return decrypt(content, secretKey);
    }

    private static String decrypt(String content, byte[] bts) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
            SecretKeySpec key = new SecretKeySpec(bts, "AES");
            cipher.init(Cipher.DECRYPT_MODE, key, spec);// 初始化

            byte[] result = cipher.doFinal(decodeBase64(content));
            return new String(result); // 解密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static AlgorithmParameterSpec getIV() {
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
        IvParameterSpec ivParameterSpec;
        ivParameterSpec = new IvParameterSpec(iv);
        return ivParameterSpec;
    }


    /**
     * Base64解码
     */
    private static byte[] decodeBase64(String s) {
        byte[] result = null;
        if (s != null) {
            try {
                result = Base64.decode(s.getBytes(), Base64.NO_WRAP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


}