package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.DO.WxTeaDO;
import team.legend.jobhunter.model.WXLogin;
import team.legend.jobhunter.model.WXUser;

@Mapper
@Component
public interface WXDao {

     @Select("SELECT COUNT(*) FROM wx_data")
     int getCount();

     @Insert("INSERT INTO wx_data(openid ,session_key, unionid, last_login, create_date, user_id) " +
             "VALUES(#{openid}, #{session_key}, #{unionid}, #{last_login}, #{create_date}, #{user_id})")
     void insertNewUser(WXLogin wxLogin);

     @Update("UPDATE wx_data SET openid = #{openid}, unionid = #{unionid}, nickname = #{nickname}," +
             "headimg_url = #{headimg_url}, gender = #{gender} WHERE user_id = #{user_id}")
     void updateUser(WXUser wxUser);

     @Update("UPDATE wx_data SET last_login = #{last_login}, session_key = #{session_key} WHERE openid = #{openid}")
     void updateLastLogin(@Param("last_login") String last_login,@Param("session_key") String session_key,@Param("openid") String openid);

     WXLogin selectLoginByUserId(String user_id);

     @Select("SELECT openid ,session_key, unionid, last_login, create_date, user_id,tea_id FROM wx_data " +
             "WHERE openid = #{openid}")
     WXLogin selectByOpenid(String openid);

     @Select("SELECT openid ,session_key, unionid, last_login, create_date, user_id,tea_id FROM wx_data " +
             "WHERE user_id = #{user_id}")
     WXLogin selectByUserId(String user_id);

     @Select("SELECT openid, unionid, user_id, nickname, headimg_url, gender FROM wx_data " +
             "WHERE user_id = #{user_id}")
     WXUser selectUserByUserId(String user_id);

     @Select("SELECT session_key FROM wx_data WHERE openid = #{openid}")
     String selectSessionKeyByOpenid(String openid);

     @Select("SELECT openid FROM wx_data WHERE user_id = #{user_id}")
     String selectOpenidByUserId(String user_id);

     @Select("SELECT user_id,nickname, headimg_url, gender, tea_id FROM wx_data " +
             "WHERE user_id = #{user_id}")
     WxTeaDO selectTeaByUserId(String user_id);

     @Update("UPDATE wx_data SET tea_id = #{tea_id}  WHERE user_id = #{user_id}")
     int updateTeaId(String user_id,String tea_id);






}
