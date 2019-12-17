package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.DO.OrderValidDO;
import team.legend.jobhunter.model.Order;
import team.legend.jobhunter.model.PreOrder;

import javax.annotation.security.PermitAll;
import java.util.List;

@Mapper
@Component
public interface OrderDao {

        int selectIsEffective(String orderId);

        @Select("SELECT * FROM orders WHERE order_id = #{order_id} ")
        Order selectByOrderId(String order_id);

        @Select("SELECT * FROM orders WHERE tea_id = #{tea_id} AND stu_id = #{stu_id} AND stu_confirm + tea_confirm <> 2 ")
        Order selectByTeaIdAndStuId(@Param("tea_id") String tea_id, @Param("stu_id") String stu_id);


        @Insert("INSERT INTO orders(order_id, tea_id, stu_id, discount, price, order_type, order_status, order_date, order_timestamp," +
                "iseffective, isonline, requirement, experience, tele, realname," +
                "tea_nickname,tea_tele,tea_tag,tea_img_url,tea_gender,tea_email,tea_description,tea_company,position)" +
                "VALUES(#{order_id}, #{tea_id}, #{stu_id}, #{discount}, #{price}, #{order_type}, #{order_status}, #{order_date}," +
                "#{order_timestamp}, #{iseffective}, #{isonline}, #{requirement}, #{experience}, #{tele}, #{realname}," +
                "#{tea_nickname},#{tea_tele},#{tea_tag},#{tea_img_url},#{tea_gender},#{tea_email},#{tea_description}," +
                "#{tea_company},#{position})")
        int insertOrder(Order order);

        @Update("UPDATE orders SET order_actual_payment = #{order_actual_payment} WHERE order_id = #{order_id}")
        void updateActual_payment(@Param("order_actual_payment") String price,@Param("order_id") String order_id);

        @Select("SELECT COUNT(*) FROM orders")
        int getCount();

        @Select("SELECT COUNT(*) FROM orders WHERE tea_id = #{tea_id} AND order_status = #{order_status}")
        int getCountByTStatus(@Param("tea_id") String user_id,@Param("order_status") int order_status);

        @Select("SELECT COUNT(*) FROM orders WHERE tea_id = #{tea_id} ")
        int getCountByTeaId(@Param("tea_id") String tea_id);

        @Select("SELECT COUNT(*) FROM orders WHERE stu_id = #{stu_id} AND order_status = #{order_status}")
        int getCountBySStatus(@Param("stu_id") String stu_id,@Param("order_status") int order_status);

        @Select("SELECT COUNT(*) FROM orders WHERE stu_id = #{stu_id} ")
        int getCountByStuId(@Param("stu_id") String stu_id);

        @Select("SELECT * FROM  orders t1 " +
        "INNER JOIN (" +
        "SELECT order_id FROM orders WHERE stu_id = #{stu_id} AND order_status = #{order_status} " +
                "ORDER BY order_timestamp DESC LIMIT #{page}, #{pagesize} " +
        ") t2 " +
        "ON t1.order_id = t2.order_id")
//        @Select("SELECT * FROM orders WHERE stu_id = #{stu_id} AND order_status = #{order_status} ORDER BY order_timestamp ,order_id DESC LIMIT #{page}, #{pagesize}")
        List<Order> selectByStuId(String stu_id,int order_status,int page,int pagesize);

//        @Select("SELECT * FROM orders WHERE tea_id = #{tea_id} AND order_status = #{order_status} ORDER BY order_timestamp DESC ")
        @Select("SELECT * FROM  orders t1 " +
        "INNER JOIN (" +
        "SELECT order_id FROM orders WHERE tea_id = #{tea_id} AND order_status = #{order_status} " +
        "ORDER BY order_timestamp DESC LIMIT #{page}, #{pagesize} " +
        ") t2 " +
        "ON t1.order_id = t2.order_id")
        List<Order> selectByTeaId(String tea_id,int order_status,int page,int pagesize);


        @Select("SELECT tea_id,order_status,order_confirm,stu_confirm, tea_confirm FROM orders WHERE order_id = #{order_id}")
        OrderValidDO selectTeaId(@Param("order_id") String orderId);

        @Select("SELECT stu_id,order_status,order_confirm,tea_confirm FROM orders WHERE order_id = #{order_id}")
        OrderValidDO selectStuId(@Param("order_id") String orderId);

        @Update("UPDATE orders SET order_timestamp = #{order_timestamp},appoint_location = #{appoint_location} , appoint_timestamp = #{appoint_timestamp} WHERE order_id = #{order_id}")
        int updateOrderInfo(@Param("order_timestamp") Long timestamp,@Param("order_id") String order_id, @Param("appoint_location") String appoint_location, @Param("appoint_timestamp") Long appoint_time);

        @Update("UPDATE orders SET order_confirm = 1 , order_timestamp = #{order_timestamp} WHERE order_id = #{order_id} ")
        int confirmOrder(@Param("order_id") String order_id,@Param("order_timestamp") Long order_timestamp);

        @Update("UPDATE orders SET stu_confirm = 1 , order_timestamp = #{order_timestamp} WHERE order_id = #{order_id} ")
        int confirmStuAccomplish(String order_id,Long order_timestamp);

        @Update("UPDATE orders SET tea_confirm = 1 , order_timestamp = #{order_timestamp} WHERE order_id = #{order_id} ")
        int confirmTeaAccomplish(String order_id,Long order_timestamp);

        @Select("SELECT stu_id, order_status,order_confirm,appoint_timestamp,appoint_location FROM orders WHERE order_id = #{order_id}")
        OrderValidDO selectValidInfo(String order_id);

        @Update("UPDATE orders SET order_status = 1 WHERE order_id = #{order_id}")
        int updateStatus(String order_id);

        @Select("SELECT * FROM orders WHERE tea_id = #{tea_id} AND order_status = 0" +
                "ORDER BY appoint_timestamp LIMIT 0 #{num}")
        List<Order> selectNowDayOrder(@Param("tea_id") String tea_id, @Param("num") int num);


}
