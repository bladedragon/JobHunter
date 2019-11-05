package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.DO.OrderDo;
import team.legend.jobhunter.model.Order;

@Mapper
@Component
public interface OrderDao {

        int selectIsEffective(String orderId);

        void insertService(String serviceId,int isonline,String service_date,String service_status,int isOrdered,
                       String tea_id,String tea_img_url,String tea_description,String tea_tag,int tea_sex);

        int isOrderedByServiceId(String serviceId);

        OrderDo selectByOrderId(String order_id);

        void insertOrder(OrderDo orderDo);

        void updateOrderStatus(int status);

        void updateActual_payment(String price);


}
