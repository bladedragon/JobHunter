package team.legend.jobhunter.service;

import team.legend.jobhunter.exception.OrderErrorException;
import team.legend.jobhunter.exception.SqlErrorException;
import team.legend.jobhunter.model.Order;

import java.util.Map;

public interface OrderService {

    Map<String, Object> createOrder(String tea_id,String stu_id,String service_id,String service_type,int price,
                                   int discount,int isonline,String realname,String tele,String experience,String requirement) throws OrderErrorException, SqlErrorException;

    Map<String,Object> createOrder(String preOrderId);

}
