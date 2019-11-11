package team.legend.jobhunter.dao;


import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.DO.ShowTeaDO;
import team.legend.jobhunter.model.DO.TeaDO;
import team.legend.jobhunter.model.DO.TeaInfoDo;

import java.util.List;


@Mapper
@Component
public interface Resume_serviceDao {



    @Select("SELECT * FROM  resume_info t1 " +
            "JOIN (SELECT service_id FROM resume_info limit #{pagesize}, #{page} ) t2 " +
            "ON t1.service_id = t2.service_id")
    List<ShowTeaDO> selectTeaInfo(int pagesize, int page);

    @Insert("INSERT INTO resume_info (" +
            "service_id,tea_id,tea_nickname,tea_img_url,tea_gender,tea_description,tea_tag," +
            "tea_company, position,isonline, service_timestamp,service_status) " +
            "VALUES (#{service_id},#{tea_id},#{tea_nickname},#{tea_img_url}, #{tea_gender},#{tea_description}," +
            "#{tea_tag},#{tea_company},#{position},#{isonline},#{service_timestamp},#{service_status}) " +
            "ON DUPLICATE KEY UPDATE tea_nickname = #{tea_nickname},tea_img_url = #{tea_img_url}," +
            "tea_description = #{tea_description}, tea_tag = #{tea_tag}, tea_company=#{tea_company}, isonline = #{isonline}," +
            "tea_gender = #{tea_gender}, service_timestamp = #{service_timestamp}, service_status = #{service_status}")
    int inertResume(TeaInfoDo teaInfoDo);


    @Delete("DELETE FROM resume_info WHERE tea_id = #{tea_id} ")
    void deleteResume(String tea_id);

    @Select("SELECT service_id FROM resume_info WHERE tea_id = #{tea_id}")
    String selectServiceId(String tea_id);

    @Select("SELECT COUNT(*) FROM resume_info")
    int getCount();




}
