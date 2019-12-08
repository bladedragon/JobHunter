package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.DO.OrderTeaDO;
import team.legend.jobhunter.model.DO.TeaDO;
import team.legend.jobhunter.model.Teacher;

@Mapper
@Component
public interface TeaDao {

    @Select("SELECT * FROM teacher WHERE tea_id = #{tea_id}")
    Teacher selectByTeaId(@Param("tea_id") String teaId);

//    @Select("SELECT tea_id,tea_nickname,tea_img_url,tea_tele,tea_email,tea_description,tea_tag," +
//            "tea_company,position,tea_type,isonline FROM teacher WHERE tea_id = #{tea_id}")
//    TeaDO selectTeaDOByTeaID(@Param("tea_id") String teaId);

    @Select("SELECT tea_nickname,tea_tag,tea_img_url,tea_gender,tea_email,tea_description,tea_company,position,isonline" +
            " FROM teacher WHERE tea_id = #{tea_id}")
    OrderTeaDO selectOrderDoBYTeaId(@Param("tea_id") String tea_id);

    @Update("UPDATE teacher SET tea_img_url = #{tea_img_url} WHERE tea_id = #{tea_id}")
     void uploadImg(String tea_id,String tea_img_url);

    @Update("UPDATE teacher SET tea_nickname = #{tea_nickname}, tea_img_url = #{tea_img_url}, " +
            "tea_tele = #{tea_tele}, tea_email = #{tea_email}, " +
            "tea_description = #{tea_description}, tea_tag = #{tea_tag}, tea_company = #{tea_company}," +
            "position = #{position}, tea_type = #{tea_type}, isonline = #{isonline} " +
            "WHERE tea_id = #{tea_id}")
    int updateTea(TeaDO teaDo);

    @Update("UPDATE teacher SET tea_nickname = #{tea_nickname},tea_tele = #{tea_tele}, " +
            "tea_description = #{tea_description}, tea_tag = #{tea_tag}, tea_company = #{tea_company}," +
            "position = #{position}, tea_type = #{tea_type}, isonline = #{isonline} " +
            "WHERE tea_id = #{tea_id}")
    int updateTeaWithoutImg(@Param("tea_id") String teaId,@Param("tea_nickname") String nickname,@Param("tea_tele") String teles,
                            @Param("tea_description") String perDes,@Param("tea_tag")String offers,
                            @Param("tea_company")String company,@Param("position")String position,
                            @Param("tea_type")String serviceTypes,@Param("isonline") Integer isOnline);

    @Insert("INSERT INTO teacher(tea_id,tea_nickname,tea_img_url,tea_gender,tea_realname,verify_code) " +
            "VALUES(#{tea_id},#{tea_nickname},#{tea_img_url},#{tea_gender},#{tea_realname},#{verify_code})")
    int insertTea(String tea_id,String tea_nickname,String tea_img_url,int tea_gender,@Param("tea_realname") String realname,String verify_code);

    @Select("SELECT tea_img_url FROM teacher WHERE tea_id = #{tea_id}")
    String selectHeadImgByTeaId(@Param("tea_id") String teaId);

    @Select("SELECT tea_Id FROM teacher WHERE tea_id = #{tea_id}")
    String selectTeaId(String tea_id);

    @Select("SELECT COUNT(*) FROM teacher")
    int getCount();






}
