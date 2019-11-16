package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.Order;
import team.legend.jobhunter.model.PreOrder;

import java.util.List;

@Component
@Mapper
public interface PreOrderDao {


    //存在重复就更新
    @Insert("INSERT INTO preorder(preorder_id, service_id, tea_id, stu_id, realname, tele, experience," +
            "requirement, isonline, create_date, order_type, expire, price, discount,timestamp) " +
            "VALUES(#{preorder_id},#{service_id},#{tea_id},#{stu_id},#{realname},#{tele},#{experience},#{requirement}," +
            "#{isonline},#{create_date},#{order_type},#{expire},#{price},#{discount},#{timestamp})")
    int insertPreOrder(PreOrder preOrder);

    @Select("SELECT expire FROM preorder WHERE preorder_id = #{preorder_id} ")
    int selectExpire(String preorder_id);

    @Select("SELECT preorder_id FROM preorder WHERE tea_id = #{tea_id} AND stu_id = #{stu_id}")
    String selectPreOrderId(@Param("tea_id") String tea_id,@Param("stu_id") String stu_id);

    @Select("SELECT * FROM preorder WHERE tea_id = #{tea_id} AND stu_id = #{stu_id}")
    PreOrder selectAllByTeaIdAndStuId(@Param("tea_id") String tea_id , @Param("stu_id") String stu_id);

    @Select("SELECT * FROM preorder WHERE preorder_id = #{preorder_id}")
    PreOrder selectAllByPreOrderId(String preorder_id);

    //只更新用户的几个字段
    @Update("UPDATE preorder SET realname = #{realname} ,tele = #{tele}, experience = #{experience}," +
            "requirement = #{requirement}, isonline = #{isonline}," +
            "price = #{price}, discount = #{discount} ,create_date = #{create_date}")
    int updatePreOrder(String realname,String tele,String experience,String requirement,
                       Integer isonline,Integer price,Integer discount,Long timestamp);

    @Select("SELECT COUNT(*) FROM preorder")
    int getCount();

    @Select("SELECT COUNT(*) FROM preorder WHERE stu_id = #{stu_id}")
    int getCountByStuId(@Param("stu_id") String stu_id);

    @Delete("DELETE FROM preorder WHERE preorder_id = #{preorder_id}")
    int deletePreOrder(String preorder_id);

    @Insert("INSERT INTO invalid_order(order_id, tea_id, stu_id, realname, tele, experience," +
            "requirement, isonline, order_date, order_type, price, discount) " +
            "VALUES(#{order_id},#{tea_id},#{stu_id},#{realname},#{tele},#{experience},#{requirement}," +
            "#{isonline},#{order_date},#{order_type},#{price},#{discount})" +
            "ON DUPLICATE KEY UPDATE order_date = #{order_date}")
    int insertInvaludOrder(String order_id,String tea_id,String stu_id,String realname,String tele,String experience,
                           String requirement,int isonline,String order_date,String order_type,int price,
                           int discount);

    @Select("SELECT * FROM preorder WHERE stu_id = #{stu_id} ORDER BY timestamp  ")
    List<PreOrder> selectByStuId(String stu_id);

    @Select("SELECT stu_id FROM preorder WHERE preorder_id = #{preorder_id}")
    String selectStuIdByPreOrderId(String preorde_id);


}
