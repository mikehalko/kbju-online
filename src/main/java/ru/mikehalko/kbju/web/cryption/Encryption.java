package ru.mikehalko.kbju.web.cryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encryption {
    static MessageDigest messageDigest;

    public static String hashing(String password) throws NoSuchAlgorithmException {
        messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }
}
