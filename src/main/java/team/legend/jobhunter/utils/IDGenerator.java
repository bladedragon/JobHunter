package team.legend.jobhunter.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.dao.TeaDao;
import team.legend.jobhunter.dao.WXDao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class IDGenerator {


    public String  createOrderId(String service_id,int index){
        int UID = UUID.randomUUID().toString().hashCode();
        if(UID<0){
            UID = -UID;
        }
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = simpleDateFormat.format(new Date());
//        String UPServiceId = service_id.toUpperCase().substring(0,5);
        String OrderId = "79"+date+String.format("%010d", UID)+index;
        return OrderId;
    }

    public String createPreOrderId(String service_id, int index){
       int UID  = UUID.randomUUID().toString().hashCode();
        if (UID < 0) {
            UID = -UID;
        }
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = simpleDateFormat.format(new Date());
//        String UpServiceId = service_id.toUpperCase().substring(0,5);
        String preOrderId = "80"+date+String.format("%010d", UID)+index;

        return preOrderId;
    }

    /**
     * 生成service_id
     * 保证teaId和serviceId的一一对应关系，因此只是对teaId进行简单的编码
     * @param tea_id
     * @return
     */
    public String createServiceId(String tea_id){
        System.out.println(tea_id);
        String origin_id ="service-"+tea_id;
        String service_id = SecretUtil.MD5Encode(origin_id).toLowerCase();
        return service_id;
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
//        System.out.println(String.valueOf(System.currentTimeMillis()).substring(0,4));
        System.out.println(UUID.randomUUID().toString().hashCode());

    }
}
