package cn.treedeep.king.shared.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 * 加密工具类，提供多种加密解密功能
 * <p>
 * 包含 RSA 非对称加密、AES 对称加密以及混合加密实现
 * </p>
 *
 * @author Rubin
 * @version 1.0
 */
public final class EncryptionUtils {
    private EncryptionUtils() {
        throw new IllegalStateException("工具类不允许实例化");
    }

    /* ------------------------- RSA 非对称加密 ------------------------- */

    /**
     * 生成 RSA 密钥对
     *
     * @return 生成的密钥对
     * @throws NoSuchAlgorithmException 如果算法不支持
     */
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        return keyPairGen.generateKeyPair();
    }

    /**
     * 生成 RSA 密钥对
     *
     * @param keySize 密钥长度，推荐 2048 或以上
     * @return 生成的密钥对
     * @throws NoSuchAlgorithmException 如果算法不支持
     */
    public static KeyPair generateRSAKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(keySize);
        return keyPairGen.generateKeyPair();
    }

    /**
     * 获取 Base64 编码的公钥字符串
     *
     * @param keyPair 密钥对
     * @return Base64 编码的公钥字符串
     */
    public static String getPublicKeyBase64(KeyPair keyPair) {
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    /**
     * 获取 Base64 编码的私钥字符串
     *
     * @param keyPair 密钥对
     * @return Base64 编码的私钥字符串
     */
    public static String getPrivateKeyBase64(KeyPair keyPair) {
        return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    /**
     * 从 Base64 字符串加载公钥
     *
     * @param publicKeyStr Base64 编码的公钥字符串
     * @return 公钥对象
     * @throws Exception 如果加载失败
     */
    public static PublicKey loadRSAPublicKey(String publicKeyStr) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 从 Base64 字符串加载私钥
     *
     * @param privateKeyStr Base64 编码的私钥字符串
     * @return 私钥对象
     * @throws Exception 如果加载失败
     */
    public static PrivateKey loadRSAPrivateKey(String privateKeyStr) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * RSA 公钥加密
     *
     * @param data      待加密数据
     * @param publicKey Base64 编码的公钥字符串
     * @return Base64 编码的加密结果
     * @throws Exception 如果加密失败
     */
    public static String rsaEncrypt(String data, String publicKey) throws Exception {
        PublicKey key = loadRSAPublicKey(publicKey);
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * RSA 私钥解密
     *
     * @param encryptedData Base64 编码的加密数据
     * @param privateKey    Base64 编码的私钥字符串
     * @return 解密后的原始数据
     * @throws Exception 如果解密失败
     */
    public static String rsaDecrypt(String encryptedData, String privateKey) throws Exception {
        PrivateKey key = loadRSAPrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /* ------------------------- AES 对称加密 ------------------------- */

    /**
     * 生成 AES 密钥
     *
     * @param keySize 密钥长度，支持 128/192/256
     * @return 生成的 AES 密钥
     * @throws NoSuchAlgorithmException 如果算法不支持
     */
    public static SecretKey generateAESKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(keySize);
        return keyGen.generateKey();
    }

    /**
     * 获取 Base64 编码的 AES 密钥字符串
     *
     * @param secretKey AES 密钥
     * @return Base64 编码的密钥字符串
     */
    public static String getAESKeyBase64(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * 从 Base64 字符串加载 AES 密钥
     *
     * @param keyStr Base64 编码的 AES 密钥字符串
     * @return AES 密钥对象
     */
    public static SecretKey loadAESKey(String keyStr) {
        byte[] keyBytes = Base64.getDecoder().decode(keyStr);
        return new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * AES 加密 (使用 GCM 模式)
     *
     * @param data 待加密数据
     * @param key  AES 密钥
     * @param iv   初始化向量 (12字节)
     * @return Base64 编码的加密结果 (格式: IV + 加密数据 + 认证标签)
     * @throws Exception 如果加密失败
     */
    public static String aesEncrypt(String data, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv); // 128位认证标签
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // 组合 IV 和加密数据
        byte[] combined = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    /**
     * AES 解密 (使用 GCM 模式)
     *
     * @param encryptedData Base64 编码的加密数据 (格式: IV + 加密数据 + 认证标签)
     * @param key           AES 密钥
     * @return 解密后的原始数据
     * @throws Exception 如果解密失败
     */
    public static String aesDecrypt(String encryptedData, SecretKey key) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encryptedData);

        // 分离 IV (前12字节) 和加密数据
        byte[] iv = Arrays.copyOfRange(combined, 0, 12);
        byte[] ciphertext = Arrays.copyOfRange(combined, 12, combined.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        return new String(cipher.doFinal(ciphertext), StandardCharsets.UTF_8);
    }

    /* ------------------------- 混合加密 ------------------------- */

    /**
     * 混合加密 (使用 RSA 加密 AES 密钥，AES 加密数据)
     *
     * @param data      待加密数据
     * @param publicKey Base64 编码的 RSA 公钥字符串
     * @return Base64 编码的加密结果 (格式: IV + RSA加密的AES密钥 + AES加密的数据)
     * @throws Exception 如果加密失败
     */
    public static String hybridEncrypt(String data, String publicKey) throws Exception {
        // 1. 生成随机 AES 密钥和 IV
        SecretKey aesKey = generateAESKey(256);
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);

        // 2. 用 AES 加密数据
        String aesEncrypted = aesEncrypt(data, aesKey, iv);
        byte[] aesEncryptedBytes = Base64.getDecoder().decode(aesEncrypted);
        byte[] encryptedData = Arrays.copyOfRange(aesEncryptedBytes, 12, aesEncryptedBytes.length);

        // 3. 用 RSA 加密 AES 密钥
        byte[] encryptedAesKey = rsaEncrypt(getAESKeyBase64(aesKey), publicKey).getBytes(StandardCharsets.UTF_8);

        // 4. 组合 IV + RSA加密的AES密钥 + AES加密的数据
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(iv);
        outputStream.write(encryptedAesKey);
        outputStream.write(encryptedData);

        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    /**
     * 混合解密
     *
     * @param encryptedData Base64 编码的加密数据
     * @param privateKey    Base64 编码的 RSA 私钥字符串
     * @return 解密后的原始数据
     * @throws Exception 如果解密失败
     */
    public static String hybridDecrypt(String encryptedData, String privateKey) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encryptedData);

        // 1. 解析 IV (前12字节)
        byte[] iv = Arrays.copyOfRange(combined, 0, 12);

        // 2. 解析 RSA 加密的 AES 密钥 (RSA 2048加密后长度为256字节)
        byte[] encryptedAesKey = Arrays.copyOfRange(combined, 12, 12 + 256);
        String aesKeyStr = rsaDecrypt(new String(encryptedAesKey, StandardCharsets.UTF_8), privateKey);
        SecretKey aesKey = loadAESKey(aesKeyStr);

        // 3. 解析 AES 加密的数据
        byte[] ciphertext = Arrays.copyOfRange(combined, 12 + 256, combined.length);

        // 4. 组合 IV 和加密数据用于 AES 解密
        byte[] aesCombined = new byte[12 + ciphertext.length];
        System.arraycopy(iv, 0, aesCombined, 0, 12);
        System.arraycopy(ciphertext, 0, aesCombined, 12, ciphertext.length);

        return aesDecrypt(Base64.getEncoder().encodeToString(aesCombined), aesKey);
    }
}
