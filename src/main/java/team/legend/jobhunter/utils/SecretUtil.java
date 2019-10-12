package team.legend.jobhunter.utils;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
public class SecretUtil {

    public static byte[] encode(String algorithm,String text,String secret){
        Mac mac = null;
        try {
            mac = Mac.getInstance(algorithm);
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(),algorithm);
            mac.init(secretKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }finally {
            if(mac != null){
                byte[]  macBuff = mac.doFinal(text.getBytes());
                return macBuff;
            }else{
                log.error("mac is null");
            }
        }
        return null;

    }

    public static String encodeBase64(String text) {
            return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
        }
    public static String decodeBase64(String text) {
        return new String(Base64.getDecoder().decode(text.getBytes()));
    }

    public static byte[] decodeBase64ToBtye(String text) {
        return Base64.getDecoder().decode(text.getBytes());
    }

    public static String encodeBase64(byte[] encode) {
        return Base64.getEncoder().encodeToString(encode);
    }
}

