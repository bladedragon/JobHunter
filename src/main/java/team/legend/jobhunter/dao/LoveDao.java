package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import sun.net.www.content.text.Generic;
import team.legend.jobhunter.model.LoveItem;

import java.util.List;

@Mapper
@Component
public interface LoveDao {

    @Options(useGeneratedKeys = true,keyColumn = "id")
    @Insert("INSERT INTO loved(stu_id,save_date,offer_id) VALUES(#{stu_id}, #{save_date}, #{offer_id})")
    int insert(String stu_id,String save_date,String offer_id);

    @Delete("DELETE FROM loved WHERE stu_id = #{stu_id} AND offer_id = #{offer_id}")
    int delete(String stu_id,String offer_id);

    @Select("SELECT * FROM loved WHERE stu_id = #{stu_id} AND offer_id = #{offer_id}")
    LoveItem selectOneByStuId(@Param("stu_id") String stu_id, @Param("offer_id") String offer_id);

//    @Select("SELECT * FROM loved WHERE stu_id = #{stu_id}")
    @Select("SELECT * FROM  loved t1 " +
            "JOIN (SELECT id FROM loved WHERE stu_id = #{stu_id} limit #{page}, #{pagesize} ) t2 " +
            "ON t1.id = t2.id")
    List<LoveItem> selectByStuId(String stu_id,int page,int pagesize);

    @Select("SELECT COUNT(*) FROM loved WHERE stu_id = #{stu_id}")
    int count(String stu_id);

}
