package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.PreOrder;

import java.util.List;

@Mapper
@Component
public interface ShowPreOrderDao {



    List<PreOrder> selectByTeaId(String tea_id);


}
