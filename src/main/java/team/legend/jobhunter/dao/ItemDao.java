package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.TimeItem;

import java.util.List;

@Mapper
@Component
public interface ItemDao {

        List<TimeItem> selectAllByServiceId(String service_id);

        int selectStatus(String service_id,String item_id);

        void updateIsordered(String item_id);


}
