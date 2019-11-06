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



    @Select("SELECT * FROM  resume_info t1 " +
            "JOIN (SELECT service_id FROM resume_info limit #{}, #{} )" +
            "ON t1.service_id = t2.service_id")
    List<TeaDO> selectTeaInfo(String service_id,int pagesize,int page);

}
