package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.DO.StuDO;

@Mapper
@Component
public interface StudentDao {

    public StuDO selectByStuId(String stuId);




}
