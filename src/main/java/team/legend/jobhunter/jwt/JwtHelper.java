package team.legend.jobhunter.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.utils.SecretUtil;

import java.lang.reflect.Field;

@Slf4j
public class JwtHelper<T> {
    private String iss;

    private String aud;

    private String sub;

    public void setIss(String iss) {
        this.iss = iss;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    private long effectiveTime;

    private String algorithm;

    private String secretKey;

    public JwtHelper(String algorithm,long effectiveTime,String secretKey){
        this.algorithm = algorithm;
        this.effectiveTime = effectiveTime;
        this.secretKey = secretKey;
    }

    public Jwt createJwt(T t){
        Jwt jwt = new Jwt(algorithm,effectiveTime);
        if(iss != null){
         jwt.setParameter("iss",iss);
        }
        if(aud != null){
            jwt.setParameter("aud",aud);
        }
        if(sub != sub){
            jwt.setParameter("sub",sub);
        }
        if(t != null) {
            Field[] fields = t.getClass().getDeclaredFields();
            log.info(">>log: fields :{}",fields);
            try {
                for (Field field : fields) {
                    field.setAccessible(true);
                    jwt.setParameter(field.getName(), String.valueOf(field.get(t)));
//                    log.info("jwt.setParameter({} ,, {});",field.getName(),String.valueOf(field.get(t)));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        jwt.initSecret(secretKey);

        return jwt;
    }

    public boolean isAuthorize(Jwt jwt){
//        log.info("isA: head:{}",jwt.getHeaderJSONStr());
        String headerJson = jwt.getHeaderJSONStr();
//        log.info("isA:pay:{}",jwt.getPayloadJSONStr());
        String playloadJson = jwt.getPayloadJSONStr();
        String secret = jwt.getSignature();

        String encodeHeader = SecretUtil.encodeBase64(headerJson);
        String encodePlayload = SecretUtil.encodeBase64(playloadJson);
        String text = SecretUtil.encodeBase64(encodeHeader + "." + encodePlayload);

        String encodeText = SecretUtil.encodeBase64(SecretUtil.jwtEncode(algorithm, text,secretKey));
//        log.info("encodeText:{}",encodeText);
//        log.info("secret:{}",secret);
        return encodeText.equals(secret);

    }

}
