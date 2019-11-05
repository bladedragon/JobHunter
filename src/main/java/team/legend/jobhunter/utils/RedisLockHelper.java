package team.legend.jobhunter.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class RedisLockHelper {

    /***
     * redis分布式上锁
     * @param targetId：目标订单ID
     * @param timestamp ：现在时间+超时时间
     */
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public Boolean lock(String targetId,String timeStamp){

        if(stringRedisTemplate.opsForValue().setIfAbsent(targetId,timeStamp)){
           return true;
        }

        String locktime = stringRedisTemplate.opsForValue().get(targetId);
        if(!StringUtils.isEmpty(locktime) && Long.parseLong(locktime) < System.currentTimeMillis()){
            String currlock = stringRedisTemplate.opsForValue().getAndSet(targetId,timeStamp);
            //判断多个线程中哪个线程拿到了最新的锁
            if(!StringUtils.isEmpty(currlock)||currlock.equals(locktime)){
                stringRedisTemplate.expire(targetId,30, TimeUnit.SECONDS);
                return true;
            }
        }
        //其他的线程拿锁失败
        return false;
    }

    /**
     * Radis 分布式解锁
     * @param targetId 目标订单ID
     * @param timestamp 当前时间+超时时间
     * @return
     */
    public Boolean unlock(String targetId,String timestamp){
        try {
            String locktime = stringRedisTemplate.opsForValue().get(targetId);
            //判断是不是当前锁
            if (locktime != null && locktime.equals(timestamp)) {
                stringRedisTemplate.opsForValue().getOperations().delete(targetId);
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("redis解锁失败，报异常");
        }
        return false;
    }


    public static void main(String[] args) {


        System.out.println(System.currentTimeMillis());
        System.out.println(System.currentTimeMillis()/1000);
    }
}
