package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.legend.jobhunter.model.DO.EvaluateDO;

import java.util.List;

@Mapper
@Component
public interface EvaluateDao {

    //意见反馈
    @Options(useGeneratedKeys = true,keyColumn = "id")
    @Insert("INSERT INTO feedback(stu_id,content,tele,create_date) " +
            "VALUES(#{stu_id}, #{content}, #{tele}, #{create_date}) " +
            "ON DUPLICATE KEY UPDATE content = #{content}")
    int insertFeedBack(String stu_id, @Param("content") String feedback, String tele, String create_date);

    @Insert("INSERT INTO evaluate (stu_id,tea_id,order_id, comments, degree, create_date) " +
            "VALUES(#{stu_id}, #{tea_id}, #{order_id}, #{comments}, #{degree}, #{create_date})")
    int insertEvalaute(String stu_id,String tea_id,String order_id,String comments,Integer degree,String create_date);

    @Select("SELECT * FROM evaluate WHERE tea_id = #{tea_id} ")
    List<EvaluateDO> selectEvaluateList(String tea_id);




}
