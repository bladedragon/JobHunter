package team.legend.jobhunter.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.dao.TeaDao;
import team.legend.jobhunter.dao.WXDao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class IDGenerator {


    public String  createOrderId(String service_id){
        return null;
    }

    public String createPreOrderId(String service_id){
        return "";
    }

    public String createServiceId(String stu_id,String tea_id){
        return "";
    }
    /**
     * 用户ID加密工具
     * @param salt
     * @param openid
     * @return
     */
    public  String user_idEncode(String salt,String openid,int rank){
        String signature_header = salt+"**"+openid.substring(4);

        String encode_id =SecretUtil.MD5Encode(signature_header).substring(0,4).toUpperCase();
        String timeId = String.valueOf(System.currentTimeMillis()).substring(2,4);
        String userId = encode_id+timeId+String.valueOf(rank+2222);
        return userId;

    }

    /**
     * 老师ID加密工具
     * @param userId
     * @param openid
     * @param rank
     * @return
     */
    public String createTeaId(String userId,String openid,int rank){

        String originStr = userId+"**"+openid;
        String encodeStr = SecretUtil.MD5Encode(originStr).substring(0,4).toUpperCase();
        String timeId = String.valueOf(System.currentTimeMillis()).substring(2,4);
        String teaId = encodeStr+timeId+String.valueOf(rank+3333);
        return teaId;
    }

    public static void main(String[] args) {
        System.out.println(String.valueOf(System.currentTimeMillis()).substring(0,4));
    }
}
