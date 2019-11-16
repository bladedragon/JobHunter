package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.LoveItem;

import java.util.List;

@Mapper
@Component
public interface LoveDao {

    @Insert("INSERT INTO love(stu_id,save_date,offer_id) VALUES(#{stu_id}, #{save_date}, #{offer_id})")
    int insert(String stu_id,String save_date,String offer_id);

    @Delete("DELETE FROM love WHERE stu_id = #{stu_id}")
    int delete(String stu_id);

    @Select("SELECT * FROM love WHERE stu_id = #(stu_id), offer_id = #{offer_id}")
    LoveItem selectOneByStuId(String stu_id,String offer_id);

    @Select("SELECT * FROM love WHERE stu_id = #{stu_id}")
    List<LoveItem> selectByStuId(String stu_id);

    @Select("SELECT COUNT(*) FROM orders WHERE stu_id = #{stu_id}")
    int count(String stu_id);
}
