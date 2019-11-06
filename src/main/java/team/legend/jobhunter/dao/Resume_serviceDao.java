package team.legend.jobhunter.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.DO.TeaDO;

import java.util.List;


@Mapper
@Component
public interface Resume_serviceDao {

    int selectIsOrderedByService_id(String service_id);

    @Update("UPDATE resume_info SET service_id = #{service_id},tea_id =#{tea_id}, tea_nickname = #{nickname}, " +
            "tea_img_url = #{tea_img_url}, tea_description= #{tea_description},tea_tag =#{tea_tag}," +
            "tea_company = #{tea_company},tea_gender = #{tea_gender],isonline = #{isonline},service_timestamp= #{service_timestamp}," +
            "position = #{position} ")
    int updateTeaInfo(String service_id,String tea_id,String tea_nickname,String tea_img_url,
                      String tea_description,String tea_tag,String tea_company,int tea_gender,
                      int isonline,long service_timestamp,int service_status,String positon);

    @Select("SELECT * FROM  resume_info t1 " +
            "JOIN (SELECT service_id FROM resume_info limit #{}, #{} )" +
            "ON t1.service_id = t2.service_id")
    List<TeaDO> selectTeaInfo(String tea_id);
}
