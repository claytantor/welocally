package com.welocally.admin.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

@Component
public class DigestUtils {
    
    public String makeMD5Hash(String unhashed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");  
        messageDigest.update(unhashed.getBytes(),0, unhashed.length());  
        String hashedPass = new BigInteger(1,messageDigest.digest()).toString(16);  
        if (hashedPass.length() < 32) {
           hashedPass = "0" + hashedPass; 
        }
        return hashedPass;
    }

}
