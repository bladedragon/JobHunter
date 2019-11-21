package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.LoveItem;

import java.util.List;

@Mapper
@Component
public interface LoveDao {

    @Insert("INSERT INTO loved(stu_id,save_date,offer_id) VALUES(#{stu_id}, #{save_date}, #{offer_id})" +
            "ON DUPLICATE KEY UPDATE offer_id = #{offer_id}, save_date = #{save_date}")
    int insert(String stu_id,String save_date,String offer_id);

    @Delete("DELETE FROM loved WHERE stu_id = #{stu_id}")
    int delete(String stu_id);

    @Select("SELECT * FROM loved WHERE stu_id = #{stu_id} AND offer_id = #{offer_id}")
    LoveItem selectOneByStuId(@Param("stu_id") String stu_id, @Param("offer_id") String offer_id);

//    @Select("SELECT * FROM loved WHERE stu_id = #{stu_id}")
    @Select("SELECT * FROM  loved t1 " +
            "JOIN (SELECT stu_id FROM loved limit #{page}, #{pagesize} ) t2 " +
            "ON t1.stu_id = t2.stu_id")
    List<LoveItem> selectByStuId(String stu_id,int page,int pagesize);

    @Select("SELECT COUNT(*) FROM orders WHERE stu_id = #{stu_id}")
    int count(String stu_id);

}
