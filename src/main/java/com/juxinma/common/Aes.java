package com.juxinma.common;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description Aes对称加密
 * @Author 黄名富
 * @Date 2022/7/25 00:15
 * @Version 1.0
 **/
public class Aes {

    private final String ALGORITHM_NAME = "AES";

    public static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;

    private final Lock lock = new ReentrantLock();

    /**
     * 密钥
     */
    private final SecretKey key;

    /**
     * 加密/解码器
     */
    private final Cipher cipher;

    private Aes() {
        this.key = generateKey();
        try {
            cipher = Cipher.getInstance(ALGORITHM_NAME);
        } catch (Exception e) {
            throw new RuntimeException("加密/解码器 初始化失败");
        }
    }

    private static Aes instance = new Aes();

    public static Aes getInstance() {
        return instance;
    }

    /**
     * 生成密钥
     *
     * @return 密钥
     */
    private SecretKey generateKey() {
        KeyGenerator keyGenerator = null;
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(SecureRandom.getSeed(16));
            keyGenerator = KeyGenerator.getInstance(ALGORITHM_NAME);
            keyGenerator.init(128,random);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有该加密算法");
        }
        return keyGenerator.generateKey();
    }

    /**
     * 加密
     *
     * @param content
     *          待加密内容
     * @return 加密内容 base64
     */
    public String encrypt(String content) {
        lock.lock();
        try {
            cipher.init(Cipher.ENCRYPT_MODE,key);
            byte[] bytes = cipher.doFinal(content.getBytes());
            return java.util.Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("加密失败");
        }finally {
            lock.unlock();
        }
        //解锁
    }

    /**
     * 解密
     *
     * @param content
     *          待解密内容 Base64表示的字符串
     * @return 解密内容 字符串
     */
    public String decrypt(String content) {
        lock.lock();
        //base64解码
        byte[] contentBytes = content.getBytes(CHARSET_UTF_8);
        try {
            cipher.init(Cipher.DECRYPT_MODE,key);
            byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(contentBytes));
            return new String(bytes,CHARSET_UTF_8);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("解密失败");
        } finally {
            lock.unlock();
        }
        //解锁
    }

}
