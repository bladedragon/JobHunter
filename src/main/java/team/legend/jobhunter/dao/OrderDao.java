package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.*;
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

        @Select("SELECT * FROM order WHERE order_id = #{order_id}")
        Order selectByOrderId(String order_id);


        @Insert("INSERT INTO order(order_id, tea_id, stu_id, discount, price, order_type, order_status, order_date, order_timestamp," +
                "iseffective, isonline, requirement, experience, tele, realname)" +
                "VALUES(#{order_id}, #{tea_id}, #{stu_id}, #{discount}, #{price}, #{order_type}, #{order_status}, #{order_date}," +
                "#{order_timestamp}, #{iseffective}, #{isonline}, #{requirement}, #{experience}, #{tele}, #{realname})")
        int insertOrder(Order order);



        void updateOrderStatus(int status);

        @Update("UPDATE order SET order_actual_payment = #{order_actual_payment} WHERE order_id = #{order_id}")
        void updateActual_payment(@Param("order_actual_payment") String price,@Param("order_id") String order_id);

        @Select("SELECT COUNT(*) FROM teacher")
        int getCount();
}
