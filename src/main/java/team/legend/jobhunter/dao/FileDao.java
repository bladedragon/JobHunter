package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.DO.FileDO;

import javax.annotation.Generated;
import javax.annotation.security.PermitAll;
import java.util.List;

@Component
@Mapper
public interface FileDao {


    @Options(useGeneratedKeys = true,keyColumn = "id")
    @Insert("INSERT INTO file_data (order_id, file_url, update_date,istea,file_name) " +
            "VALUES(#{order_id},#{file_url}, #{update_date},#{istea},#{file_name})")
    int insertFileUrl(@Param("order_id") String orderId, @Param("file_url") String file_url,
                      @Param("update_date") String update_date,@Param("istea") int isTea,@Param("file_name")String file_name);

    @Select("SELECT file_name , file_url FROM file_data WHERE order_id = #{order_id} AND istea = #{istea}")
    List<FileDO> selectFilePath(String order_id,@Param("istea") int isTea);

}
