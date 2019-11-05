package team.legend.jobhunter.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import team.legend.jobhunter.dao.ItemDao;
import team.legend.jobhunter.dao.OrderDao;
import team.legend.jobhunter.dao.TeaDao;
import team.legend.jobhunter.exception.OrderErrorException;
import team.legend.jobhunter.model.DO.OrderDo;
import team.legend.jobhunter.model.DO.TeaDO;
import team.legend.jobhunter.model.Order;
import team.legend.jobhunter.model.PreOrder;
import team.legend.jobhunter.service.OrderService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;
import team.legend.jobhunter.utils.RedisLockHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    RedisTemplate<String, PreOrder> preOrderRedisTemplate;
    @Autowired
    RedisLockHelper redisLockHelper;
    @Autowired
    ItemDao itemDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    TeaDao teaDao;

    @Override
    public Map<String, Order> createOrder(String service_type,String service_id,String preOrder_id,String order_id) throws OrderErrorException {
        Map<String,Order> res_map = new HashMap<>(10);
        String lockTimestamp = String.valueOf(System.currentTimeMillis()+ Constant.LOCK_EXPIRE_TIME);
        redisLockHelper.lock(order_id,lockTimestamp);

        PreOrder preOrder = preOrderRedisTemplate.opsForValue().get(service_id);
        if(preOrder!=null && preOrder.getOrder_id().equals(order_id)){
            int item_isOrdered = itemDao.selectStatus(service_id,preOrder.getItem_id());
            if(item_isOrdered != 0 ){
                res_map.put("be ordered",null);
                redisLockHelper.unlock(order_id,lockTimestamp);
                return res_map;
            }
            TeaDO teaDO = teaDao.selectByTeaId(preOrder.getTea_id());
            orderDao.insertOrder(new OrderDo(order_id,teaDO.getTeaId(),teaDO.getNickname(),teaDO.getImg_url(),teaDO.getGender(),
                    preOrder.getStu_id(),preOrder.getItem_time(),preOrder.getItem_price(),preOrder.getItem_isonline(),
                    preOrder.getItem_time_detail(),CommonUtil.getNowDate(),preOrder.getOrder_type(),200,CommonUtil.getNowDate(),
                   null,0));
            itemDao.updateIsordered(preOrder.getItem_id());
                OrderDo orderDo= orderDao.selectByOrderId(order_id);
                if(orderDo == null){
                    throw new OrderErrorException("生成订单失败");
            }
                //返回订单逻辑
            //当预订单不存在的时候怎么办？
            //当订单存在的时候怎么办？
            //创造订单必须在创建预订单之后，但是其中加两次锁是否会出现异常情况
            //

        }else{
            res_map.put("preorder is null",null);

        }

        redisLockHelper.unlock(order_id,lockTimestamp);
        return res_map;

    }

    public static void main(String[] args) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd/HH-mm");
        String datetime = simpleDateFormat.format(new Date());
        System.out.println(datetime);
    }
}
