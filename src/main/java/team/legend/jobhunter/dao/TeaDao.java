package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.DO.TeaDO;
import team.legend.jobhunter.model.Teacher;

@Mapper
@Component
public interface TeaDao {

    @Select("SELECT * FROM teacher WHERE tea_id = #{tea_id}")
    Teacher selectByTeaId(@Param("tea_id") String teaId);

    @Update("UPDATE teacher SET tea_img_url = #{tea_img_url} WHERE tea_id = #{tea_id}")
     void uploadImg(String tea_id,String tea_img_url);

    @Update("UPDATE teacher SET tea_nickname = #{tea_nickname}, tea_realname = #{tea_realname}, " +
            "tea_img_url = #{tea_img_url}, tea_tele = #{tea_tele}, tea_email = #{tea_email}, " +
            "tea_description = #{tea_description}, tea_tag = #{tea_tag}, tea_company = #{tea_company}," +
            "position = #{position}, tea_type = #{tea_type}, isonline = #{isonline} " +
            "WHERE tea_id = #{tea_id}")
    int updateTea(TeaDO teaDo);

    @Insert("INSERT INTO teacher(tea_id,verify_code) VALUES(#{tea_id},#{verify_code})")
    int insertTea(String tea_id,String verify_code);

    @Select("SELECT tea_img_url FROM teacher WHERE tea_id = #{tea_id}")
    String selectHeadImgByTeaId(@Param("tea_id") String teaId);

    @Select("SELECT tea_Id FROM teacher WHERE tea_id = #{tea_id}")
    String selectTeaId(String tea_id);

    @Select("SELECT COUNT(*) FROM teacher")
    int getCount();



}
