package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import javax.annotation.Generated;
import javax.annotation.security.PermitAll;
import java.util.List;

@Component
@Mapper
public interface FileDao {


    @Options(useGeneratedKeys = true,keyColumn = "id")
    @Insert("INSERT INTO file_data (order_id, file_url, update_date,iseffective) " +
            "VALUES(#{order_id},#{file_url}, #{update_date},#{iseffective})")
    int insertFileUrl(@Param("order_id") String orderId, @Param("file_url") String file_url,
                      @Param("update_date") String update_date,@Param("iseffective") int iseffective);

    @Select("SELECT * FROM file_data WHERE order_id = #{order_id}")
    List<String> selectFilePath(String order_id);
}
