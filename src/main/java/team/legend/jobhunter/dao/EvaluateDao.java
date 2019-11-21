package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.legend.jobhunter.model.DO.EvaluateDO;

import java.util.List;

@Mapper
@Component
public interface EvaluateDao {

    //意见反馈
    @Options(useGeneratedKeys = true,keyColumn = "id")
    @Insert("INSERT INTO feedback(stu_id,feedback,tele,create_date) " +
            "VALUES(#{stu_id}, #feedback}, #{tele}, #{create_date})")
    int insertFeedBack(String stu_id,String feedback,String tele,String create_date);

    @Insert("INSERT INTO evaluate (stu_id,tea_id,order_id,comment,degree,create_date), " +
            "VALUES(#{stu_id},#{tea_id},#{order_id},#{comment},#{degree}, #{create_date})")
    int insertEvalaute(String stu_id,String tea_id,String order_id,String comment,Integer degree,String create_date);

    @Select("SELECT * FROM evaluate WHERE tea_id = #{tea_id} ")
    List<EvaluateDO> selectEvaluateList(String tea_id);




}
