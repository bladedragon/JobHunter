package team.legend.jobhunter.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import team.legend.jobhunter.exception.SqlErrorException;

import java.util.Map;


public interface TeaInfoService {

     String saveImg(String tea_id,MultipartFile headImg);

     Map<String,Object> modifyInfo(JSONObject jsonObject, String img_url);

     Map<String,Object> getTeaInfo(String teaId);

     String verify(String openid, String verifyCode, String userId) throws SqlErrorException;
}
