package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.DO.TeaDO;

@Mapper
@Component
public interface TeaDao {

    public TeaDO selectByTeaId(String TeaId);

}
