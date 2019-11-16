package team.legend.jobhunter.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.legend.jobhunter.dao.OrderDao;
import team.legend.jobhunter.exception.SqlErrorException;
import team.legend.jobhunter.model.DO.OrderValidDO;
import team.legend.jobhunter.model.Order;
import team.legend.jobhunter.service.ConfirmService;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ConfirmServiceImpl  implements ConfirmService {

    @Autowired
    OrderDao orderDao;

    @Override
    public Map<String, String> teaFill(String teaId, String orderId, String location, Long appointTime) throws SqlErrorException {

        Map<String,String> response = new HashMap<>(2);
        OrderValidDO teaIdInBase = orderDao.selectTeaId(orderId);
        if(teaIdInBase.getOrder_status() == 1 || teaIdInBase.getOrder_confirm() ==1){
            log.error(">>log:teaFill: order_status[{}],order_confirm[{}]",teaIdInBase.getOrder_status(),teaIdInBase.getOrder_confirm());
            response.put("invalid","commit invalid");
            return response;
        }
        if(teaIdInBase ==null || !teaIdInBase.getTea_id().equals(teaId)){
            log.error(">>log: teaFill: teaId[{}] is null or not belong this order",teaId);
            response.put("nullError","teacher is null or not belong this order");
            return response;
        }

        int num = orderDao.updateOrderInfo(System.currentTimeMillis()/1000,orderId,location,appointTime);
        if(num ==1){
            response.put("success",null);
            return response;
        }else{
            throw new SqlErrorException("update fail");
        }
    }

    @Override
    public int confirm(String userId, String orderId) {
        OrderValidDO order = orderDao.selectValidInfo(orderId);
        if(order ==null || order.getOrder_status() ==1 || !userId.equals(order.getStu_id())){
            log.error(">>log: confirm order [{}] is null  or orderStatus is[{}] or stuId is not matched [{}] ",orderId,order.getOrder_status(),order.getStu_id());
            return 1;
        }
        if(!order.getAppoint_location().equals("") && order.getAppoint_time()!=0){
            if(order.getOrder_confirm() != 1){
                orderDao.confirmOrder(orderId,System.currentTimeMillis()/1000);
                return 0;
            }else{
                log.error(">>log: order has been confirmed");
                return 3;
            }
        }else{
            log.error(">>log: info is not filled");
            return 2;
        }

    }

    @Override
    @Transactional(rollbackFor = SqlErrorException.class)
    public Map<String, String> confirmAccomplish(String userId, String orderId,int isTea) throws SqlErrorException {
        Map<String,String> response = new HashMap<>(2);
        int num = 0;
        //以下是老师确认
        if(isTea==1){
            OrderValidDO teaIdInBase = orderDao.selectTeaId(orderId);
            if(teaIdInBase.getOrder_status() == 1 || teaIdInBase.getOrder_confirm() ==1){
                log.error(">>log:teaFill: order_status[{}],order_confirm[{}]",teaIdInBase.getOrder_status(),teaIdInBase.getOrder_confirm());
                response.put("invalid","commit invalid");
                return response;
            }
            if(teaIdInBase ==null || !teaIdInBase.getTea_id().equals(userId)){
                log.error(">>log: teaConfirm: teaId[{}] is null or not belong this order",userId);
                response.put("error","teacher is null or not belong this order");
                return response;
            }

            try {
                num = orderDao.confirmTeaAccomplish(orderId,System.currentTimeMillis()/1000);
                if (teaIdInBase.getStu_confirm() == 1) {
                    num  += orderDao.updateStatus(orderId);
                }
            }catch (Exception e){
                throw new SqlErrorException("sql exception");
            }

            //以下是学生确认
        }else {
            OrderValidDO stuIdInBase = orderDao.selectStuId(orderId);
            if(stuIdInBase.getOrder_status() == 1 || stuIdInBase.getOrder_confirm() ==1){
                log.error(">>log:stuConfirm: order_status[{}],order_confirm[{}]",stuIdInBase.getOrder_status(),stuIdInBase.getOrder_confirm());
                response.put("invalid","commit invalid");
                return response;
            }
            if (stuIdInBase == null || !stuIdInBase.getStu_id().equals(userId)) {
                log.error(">>log: stuConfirm: stuId[{}] is null or not belong this order", userId);
                response.put("error", "student is null or not belong this order");
                return response;
            }

            try {
                num = orderDao.confirmStuAccomplish(orderId,System.currentTimeMillis()/1000);
                if (stuIdInBase.getTea_confirm() == 1) {
                    num  += orderDao.updateStatus(orderId);
                }
            }catch (Exception e){
                e.printStackTrace();
                throw new SqlErrorException("sql exception");
            }

        }

        System.out.println(num);
        if(num ==2){
            response.put("success","success and status changed");
            return response;
        }else if(num ==1){
            response.put("success","success ");
            return response;
        }else{
            log.error(">>log: ConformAccomplish: update num  [{}]",num);
            throw new SqlErrorException("update fail");
        }

    }
}
