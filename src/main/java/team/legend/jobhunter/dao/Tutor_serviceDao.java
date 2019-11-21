package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.DO.ShowTeaDO;
import team.legend.jobhunter.model.DO.TeaInfoDo;
import team.legend.jobhunter.model.Order;

import java.util.List;

@Mapper
@Component
public interface Tutor_serviceDao {


    @Select("SELECT service_id FROM tutor_info WHERE tea_id = #{tea_id}")
    String selectServiceId(String tea_id);

    @Insert("INSERT INTO tutor_info (" +
            "service_id,tea_id,tea_nickname,tea_img_url,tea_gender,tea_description,tea_tag," +
            "tea_company, position,isonline, service_timestamp,service_status) " +
            "VALUES (#{service_id},#{tea_id},#{tea_nickname},#{tea_img_url}, #{tea_gender},#{tea_description}," +
            "#{tea_tag},#{tea_company},#{position},#{isonline},#{service_timestamp},#{service_status}) " +
            "ON DUPLICATE KEY UPDATE tea_nickname = #{tea_nickname},tea_img_url = #{tea_img_url}," +
            "tea_description = #{tea_description}, tea_tag = #{tea_tag}, tea_company=#{tea_company}, isonline = #{isonline}," +
            "tea_gender = #{tea_gender}, service_timestamp = #{service_timestamp}, service_status = #{service_status}")
    int insertTutor(TeaInfoDo teaInfoDo);

    @Delete("DELETE FROM tutor_info WHERE tea_id = #{tea_id}")
    void deleteTutor(String tea_id);

    @Select("SELECT COUNT(*) FROM tutor_info")
    int getCount();

    @Select("SELECT * FROM  tutor_info t1 " +
            "JOIN (SELECT service_id FROM resume_info limit #{page}, #{pagesize } ) t2 " +
            "ON t1.service_id = t2.service_id")
    List<ShowTeaDO> selectTeaInfo(int pagesize, int page);


}
