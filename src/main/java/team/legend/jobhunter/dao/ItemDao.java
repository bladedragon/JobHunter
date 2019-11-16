package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ItemDao {
        //服务信息主页填写老师已经预约时间

//        List<TimeItem> selectAllByServiceId(String service_id);

        int selectStatus(String service_id,String item_id);

        void updateIsordered(String item_id);


}
