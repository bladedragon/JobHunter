package team.legend.jobhunter.utils;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Base64;

import static io.netty.util.internal.StringUtil.byteToHexString;

@Slf4j
public class SecretUtil {

    /**
     * jwt加密工具
     * @param algorithm
     * @param text
     * @param secret
     * @return
     */
    public static byte[] jwtEncode(String algorithm,String text,String secret){
        Mac mac  = null;
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
                log.error("/nmac is null/n");
            }
        }
        return null;

    }



    public static String MD5Encode(String str){
        String encodeStr = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            encodeStr = byteArrayToHexString(md.digest(str.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 微信敏感数据解密工具
     * @param sSrc
     * @param sKey
     * @param ivs
     * @return
     */
    public static String decrypt(String sSrc, byte[] sKey, byte[] ivs) {

        Security.addProvider(new BouncyCastleProvider());
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            IvParameterSpec iv = new IvParameterSpec(ivs);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(SecretUtil.decodeBase64ToBtye(sSrc));
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    /**
     * 十六进制转换
     * @param b
     * @return
     */
    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
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

    public static String shaCheck(String rawData,String sessionKey){

        String encodeStr = rawData+sessionKey;

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.update(encodeStr.getBytes());
            byte[] shaBin= sha.digest();

            return getFormattedText(shaBin);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;

    }

    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f' };

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }


    public static void main(String[] args) {

//        byte[] bytes= jwtEncode("HmacSHA256","text","ICT_TEAM");
//        System.out.println(encodeBase64(bytes));
//        System.out.println(System.currentTimeMillis()/1000);
        String rawData = "{\"nickName\":\"！！？\",\"gender\":0,\"language\":\"zh_CN\",\"city\":\"\",\"province\":\"\",\"country\":\"\",\"avatarUrl\":\"https://wx.qlogo.cn/mmopen/vi_32/wSkpLicAbWAx4rcvyhN6n6Q387uxXESXrXgwApTFr6x5o2ZjYqt7SplI1jXKRM3IbbMlCicULKaRjFtDXfQHhpJA/132\"}";
        String sign = "51dbdb4a8ff94545ce2e1e264c6547ae47d6028e";
        System.out.println(shaCheck(rawData,sign));

    }
}

