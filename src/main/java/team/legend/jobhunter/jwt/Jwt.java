package team.legend.jobhunter.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import team.legend.jobhunter.utils.SecretUtil;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class Jwt {
    private Map<String,String> header;
    private Map<String,String> payload;
    private String signature;

    public String getHeaderJSONStr() {
        return JSON.toJSONString(header);
    }

    public String getPayloadJSONStr() {
        return JSON.toJSONString(payload);
    }

    public  String getSignature(){
        return signature;
    }

    public Jwt(String headerStr, String payloadStr, String signature){
        header = JSON.parseObject(headerStr,LinkedHashMap.class);
        payload = JSON.parseObject(payloadStr,LinkedHashMap.class);
        this.signature = signature;
    }

    public Jwt(){
        throw  new RuntimeException("cannot use empty constructor");
    }

    public Jwt(String algorithm, long effectiveTime){
        header = new LinkedHashMap<>();
        payload = new LinkedHashMap<>();

        header.put("alg",algorithm);
        header.put("tye","JWT");

        refreshEffectiveTime(effectiveTime);
    }

    public void refreshEffectiveTime(long effectiveTime){
        Date date = new Date();
        long nowTime = date.getTime()/1000;
        payload.put("exp",String.valueOf(nowTime));
        payload.put("nbf",String.valueOf(nowTime+effectiveTime));

    }

    public Boolean isOverTime(){
        Date date = new Date();
        long nowTime = date.getTime() / 1000;
        long jwtExp = Long.parseLong(payload.get("exp"));
        long jwtNbf = Long.parseLong(payload.get("nbf"));
        return nowTime > jwtExp || nowTime < jwtNbf;
    }


    public void setParameter(String key,String value){
        payload.put(key,value);
    }

    public Object getParameter(String key) {
        return payload.get(key);
    }

    public void initSecret(String key) {
        String text = SecretUtil.encodeBase64(getHeaderJSONStr()) + "." + SecretUtil.encodeBase64(getPayloadJSONStr());
        signature = SecretUtil.encodeBase64(SecretUtil.jwtEncode(header.get("alg"), text, key));
    }


    public  String getJwtString(){
        if(signature != null){
            String headerStr = getHeaderJSONStr();
            String payloadStr = getPayloadJSONStr();
            String jwtStr =  headerStr+'.'+payloadStr+"."+signature;
            return  SecretUtil.encodeBase64(jwtStr);
        }
        throw new RuntimeException("signature is not initialized");
    }

    public static Jwt fromString(String jwtStr) {
        Jwt jwt = null;
        if (jwtStr != null) {
            String jwt_decode = SecretUtil.decodeBase64(jwtStr);
            String[] str = jwt_decode.split("\\.");
            try {
                String header = SecretUtil.decodeBase64(str[0]);
                String payload = SecretUtil.decodeBase64(str[1]);
                String secret = str[2];
                jwt = new Jwt(header, payload, secret);
            } catch (Throwable e) {
                log.error(">>JWT转换失败"+jwt_decode);
            }
        }
        return jwt;
    }

    public static void main(String[] args) {
        SimpleDateFormat format = new SimpleDateFormat("HH:MM:SS");
        System.out.println(format.format(new Date()));
        Date date = new Date();
        System.out.println(date.toString());
        System.out.println(System.currentTimeMillis());
        System.out.println(date.getTime()/1000);
    }
}
