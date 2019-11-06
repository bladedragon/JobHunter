package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.Order;

@Mapper
@Component
public interface Tutor_serviceDao {

    int selectIsOrderedByService_id(String service_id);





}
