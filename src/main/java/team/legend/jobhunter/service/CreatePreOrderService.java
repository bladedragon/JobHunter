package team.legend.jobhunter.service;

import team.legend.jobhunter.model.PreOrder;
import team.legend.jobhunter.model.TimeItem;

import java.util.Map;

public interface CreatePreOrderService {


    public Map<String, PreOrder> createPreOrder(String service_type, String service_id, String stu_id, String tea_id, TimeItem timeItem);


}