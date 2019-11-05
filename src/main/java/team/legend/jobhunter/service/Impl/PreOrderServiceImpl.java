package team.legend.jobhunter.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import team.legend.jobhunter.dao.ItemDao;
import team.legend.jobhunter.model.PreOrder;
import team.legend.jobhunter.model.TimeItem;
import team.legend.jobhunter.service.CreatePreOrderService;
import team.legend.jobhunter.utils.Constant;
import team.legend.jobhunter.utils.IDGenerator;
import team.legend.jobhunter.utils.PriceUtil;
import team.legend.jobhunter.utils.RedisLockHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 逻辑要大改
 */

@Service
public class PreOrderServiceImpl implements CreatePreOrderService {

    @Autowired
    RedisLockHelper redisLockHelper;
    @Autowired
    RedisTemplate<String,PreOrder> preOrderRedisTemplate;
    @Autowired
    IDGenerator idGenerator;
    @Autowired
    ItemDao itemDao;

    @Override
    public Map<String, PreOrder> createPreOrder(String service_type,String service_id, String stu_id, String tea_id,TimeItem timeItem) {
        Map<String,PreOrder> res_map = new HashMap<>(10);

        String lockTimestamp = String.valueOf(System.currentTimeMillis()+ Constant.LOCK_EXPIRE_TIME);

        redisLockHelper.lock(service_id,lockTimestamp);

        int isOrdered = itemDao.selectStatus(service_id,timeItem.getItem_id());
        if(isOrdered != 0 ){
            res_map.put("be ordered!",null);
            redisLockHelper.unlock(service_id,lockTimestamp);
            return res_map;
        }

        PreOrder preOrder = preOrderRedisTemplate.opsForValue().get(service_id);
        if(preOrder == null){
            //获取ID计算公式等待完善
            String preOrderId = idGenerator.createPreOrderId(service_id);
            String orderId = idGenerator.createOrderId(service_id);
            //价格计算公式等待完善
            String price = PriceUtil.getPrice(timeItem.getItem_origin_price(),timeItem.getItem_discount());
            String currTime = String.valueOf(System.currentTimeMillis()/1000);
            preOrder = new PreOrder(preOrderId,orderId,service_id,tea_id,stu_id,timeItem.getItem_id(),timeItem.getItem_time(),
                    price,timeItem.getIsonline(),timeItem.getItem_time_detail(),currTime
                    ,service_type,System.currentTimeMillis()/1000);
            //插入redis数据库
            preOrderRedisTemplate.opsForValue().set(service_id,preOrder);
            preOrderRedisTemplate.expire(service_id,2, TimeUnit.MINUTES);
            PreOrder preOrder_res = preOrderRedisTemplate.opsForValue().get(service_id);
            res_map.put("success",preOrder_res);

        }else{
            res_map.put("be created!",preOrder);
        }

        redisLockHelper.unlock(service_id,lockTimestamp);
        return res_map;
    }


}
