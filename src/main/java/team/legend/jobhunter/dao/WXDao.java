package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.WXLogin;
import team.legend.jobhunter.model.WXUser;
import team.legend.jobhunter.model.response.WxLoginResponse;

@Mapper
@Component
public interface WXDao {

     @Select("SELECT COUNT(*) FROM wx_data")
     int getCount();

     void insertNewUser(WXLogin wxLogin);

     void updateUser(WXUser wxUser);

     void updateLastLogin(String last_login,String session_key);

     WXLogin selectLoginByUserId(String user_id);

     WXLogin selectByOpenid(String openid);

     WXUser selectUserByUserId(String user_id);

     @Select("SELECT session_key FROM wx_data WHERE openid = #{openid}")
     String selectSessionKeyByOpenid(String openid);
     @Select("SELECT openid FROM wx_data WHERE user_id = #{user_id}")
     String selectOpenidByUserId(String user_id);

     @Select("SELECT tea_id FROM wx_data WHERE user_id = #{user_id}")
     String selectTeaIdByUserId(String user_id);

     @Update("UPDATE wx_data SET tea_id = #{tea_id}  WHERE user_id = #{user_id}")
     int updateTeaId(String user_id,String tea_id);






}
