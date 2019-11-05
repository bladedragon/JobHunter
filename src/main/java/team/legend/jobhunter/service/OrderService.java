package team.legend.jobhunter.service;

import team.legend.jobhunter.exception.OrderErrorException;
import team.legend.jobhunter.model.Order;

import java.util.Map;

public interface OrderService {

    Map<String, Order> createOrder(String service_type,String service_id, String preOrder_id, String Order_id) throws OrderErrorException;



}
