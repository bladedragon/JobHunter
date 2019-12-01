package team.legend.jobhunter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.legend.jobhunter.jwt.JwtHelper;
import team.legend.jobhunter.model.WXLogin;
import team.legend.jobhunter.model.WXUser;

@Configuration
public class JwtConfig {


    private static final String ALGORITHM = "HmacSHA256";
    private static final String ISS = "legend";
    private static final String SUB = "WeixinUser";
    private static final String AUD = "student";

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.effectiveTime}")
    private long effectiveTime;


    @Bean("wxJwtHelper")
    public JwtHelper<WXLogin> wxJwtHelper(){
        JwtHelper<WXLogin> jwtHelper = new JwtHelper(ALGORITHM,effectiveTime,secretKey);
        jwtHelper.setSub(SUB);
        jwtHelper.setAud(AUD);
        jwtHelper.setIss(ISS);
        return jwtHelper;
    }

}
