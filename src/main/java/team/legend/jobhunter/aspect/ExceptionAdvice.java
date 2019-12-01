package team.legend.jobhunter.aspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.legend.jobhunter.exception.*;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;

import javax.servlet.http.HttpServletRequest;

//@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request,Exception e){
        if(e.getClass() == AuthorizeErrorException.class){
            log.error(">>log: login authorize fail ,message : {} ",e.getMessage());
            return CommonUtil.returnFormatSimp(Constant.AUTHORIZE_FAIL,e.getMessage());
        }
        if(e.getClass() == ParamErrorException.class){
            log.error(">>log: paran is error ,url: {} ,param: {}, message : {}",request.getRequestURI(),request.getParameterMap().toString(),
                    e.getMessage());
            return  CommonUtil.returnFormatSimp(Constant.PARAM_CODE,e.getMessage());
        }
        if(e.getClass() == SqlErrorException.class){
            log.error(">>log: SQL meets error, url: {},message: {}",request.getRequestURI(),e.getMessage());
        }
        if(e.getClass() == UploadException.class){
            log.error(">>log: file upload fail,url:{}, message: {}",request.getRequestURI(),e.getMessage());
            return CommonUtil.returnFormatSimp(Constant.UPLOAD_ERROR,e.getMessage());
        }
//        if(e.getClass() == HttpReqException.class){
//            log.error(">>log: request url fail");
//            return CommonUtil.returnFormatSimp(Constant.ERROR_CODE,"request "+e.getMessage()+" fail !");
//        }
        log.error(">>log: unKnow Exception ,message:{}",e.getMessage());
        log.error("{}",e.getStackTrace());
        return CommonUtil.returnFormatSimp(Constant.ERROR_CODE,"Unknow ERROR !");

    }
}
