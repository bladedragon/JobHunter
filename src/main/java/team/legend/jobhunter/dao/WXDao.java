package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.WXLogin;
import team.legend.jobhunter.model.WXUser;
import team.legend.jobhunter.model.response.WxLoginResponse;

@Mapper
@Component
public interface WXDao {

     void insertNewUser(WXLogin wxLogin);

     void updateUser(WXUser wxUser);

     void updateLastLogin(String last_login,String session_key);

     WXLogin selectLoginByUserId(String user_id);

     WXLogin selectByOpenid(String openid);

     WXUser selectUserByUserId(String user_id);

     String selectSessionKeyByOpenid(String openid);

     String selectOpenidByUserId(String user_id);






}
