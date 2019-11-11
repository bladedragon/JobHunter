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

    @Insert("INSERT INTO teacher(tea_id,tea_nickname,tea_img_url,tea_gender,verify_code) " +
            "VALUES(#{tea_id},#{tea_nickname},#{tea_img_url},#{tea_gender},#{verify_code})")
    int insertTea(String tea_id,String tea_nickname,String tea_img_url,int tea_gender,String verify_code);

    @Select("SELECT tea_img_url FROM teacher WHERE tea_id = #{tea_id}")
    String selectHeadImgByTeaId(@Param("tea_id") String teaId);

    @Select("SELECT tea_Id FROM teacher WHERE tea_id = #{tea_id}")
    String selectTeaId(String tea_id);

    @Select("SELECT COUNT(*) FROM teacher")
    int getCount();


    /**已被代替
     * @param tea_id
     * @param tea_nickname
     * @param tea_img_url
     * @param tea_description
     * @param tea_tag
     * @param tea_company
     * @param isonline
     * @param service_timestamp
     * @param positon
     * @return
     */
//    @Update("UPDATE resume_info SET tea_nickname = #{nickname}, " +
//            "tea_img_url = #{tea_img_url}, tea_description= #{tea_description},tea_tag =#{tea_tag}," +
//            "tea_company = #{tea_company},isonline = #{isonline},service_timestamp= #{service_timestamp}," +
//            "position = #{position} " +
//            "WHERE tea_id = #{tea_id} ")
//    int updateResumeInfo(String tea_id,String tea_nickname,String tea_img_url,
//                      String tea_description,String tea_tag,String tea_company,
//                      int isonline,long service_timestamp,String positon);
//
//    @Update("UPDATE tutor_info SET tea_nickname = #{nickname}, " +
//            "tea_img_url = #{tea_img_url}, tea_description= #{tea_description},tea_tag =#{tea_tag}," +
//            "tea_company = #{tea_company},isonline = #{isonline},service_timestamp= #{service_timestamp}," +
//            "position = #{position} " +
//            "WHERE tea_id = #{tea_id} ")
//    int updateTutorInfo(String tea_id,String tea_nickname,String tea_img_url,
//                         String tea_description,String tea_tag,String tea_company,
//                         int isonline,long service_timestamp,String positon);



}
