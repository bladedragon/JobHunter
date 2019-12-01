package team.legend.jobhunter.intercepter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import team.legend.jobhunter.jwt.Jwt;
import team.legend.jobhunter.jwt.JwtHelper;
import team.legend.jobhunter.model.WXLogin;
import team.legend.jobhunter.model.WXUser;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class JwtIntercepter implements HandlerInterceptor {

    @Autowired
    private JwtHelper<WXLogin> WXUserJwtHelper;


    private static final Pattern pattern = Pattern.compile("token\\s+(.*?)");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(">>request URL:{}",request.getRequestURI());

        String authorization = request.getHeader("Authorization");

        Matcher matcher = pattern.matcher(authorization);
//        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = null;
        if(matcher.find()){
            String jwtInHeader = matcher.group(1);
            Jwt jwt = Jwt.fromString(jwtInHeader);
            if(WXUserJwtHelper.isAuthorize(jwt)){
                if(jwt.isOverTime()){
                    log.warn(">>jwt:[{}] is time over",jwt.getParameter("user_id"));

                    try{
                        writer = response.getWriter();
                        writer.append(CommonUtil.returnFormatSimp(Constant.ERROR_JWT_TIMWOUT,"time over"));
                    }catch (Exception e){
                        e.printStackTrace();
                        response.sendError(500);
                    }
                    return false;
                }else{
                    log.info(">>jwt:[{}] authorize successfully",jwt.getParameter("user_id"));
                    request.setAttribute("user_id",jwt.getParameter("user_id"));
                    return true;
                }
            }else{
                log.warn(">>jwt:[{}] authorize fail",jwt.getParameter("user_id"));

                try{
                    writer = response.getWriter();
                    writer.append(CommonUtil.returnFormatSimp(Constant.ERROR_JWT_AUTHORIZE_FAIL,"authorize fail"));
                }catch (Exception e){
                    e.printStackTrace();
                    response.sendError(500);
                }
                return false;
            }
        }
        //项目预计部署在nginx服务器上
        log.warn(">>jwt:cannot find jwt,request ip is [{}]",request.getHeader("X-Forwarded-For"));
//        response.setContentType("application/json; charset=utf-8");
        try{
            writer = response.getWriter();
            writer.append(CommonUtil.returnFormatSimp(Constant.ERROR_JWT_NOTEXIST,"cannot find token"));
        }catch (Exception e){
            e.printStackTrace();
            response.sendError(500);
        }
        return false;
    }


}
