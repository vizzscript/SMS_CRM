package com.pinnacle.backend.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {
    // Use a valid 16-byte AES key (128-bit)
    private static final String ENCRYPTION_KEY = "517345ZA28ABAAEF"; // Exactly 16 bytes

    // Encrypt data
    public static String encrypt(String data) throws Exception {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data cannot be null or empty");
        }

        // Generate AES Secret Key
        SecretKey secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");

        // Initialize Cipher in AES/ECB mode
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Perform encryption
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());

        // Return Base64-encoded string
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Convert secret key to Base64 string
    public static String keyToString(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }



    // Encrypt API-Key
    public static String encryptedAPIKey(String apiKey){
        // Checking if apiKey is null or empty
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("Data cannot be null or empty");
        }

        // Generate AES Key
        SecretKey secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(apiKey.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
