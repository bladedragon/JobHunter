package team.legend.jobhunter.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import team.legend.jobhunter.schedule.AsTokenTask;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class AccessTokenHelper {
    @Autowired(required = true)
    RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;


    public void saveKey(String tokenType, String Token) {
        this.stringRedisTemplate.opsForValue().set(tokenType, Token, 5400, TimeUnit.SECONDS);

    }

    public String getKey(String key) {
        String str;
        try {
            str = this.stringRedisTemplate.opsForValue().get(key);
            if(str==null){
                log.warn(">>log: access_token is null in redis");
                return "empty";
            }
        }catch(NullPointerException e){
            log.error(">>log:access_token is null -> NullPoint Exception");
            return "empty";
        }

        return str;
    }





}
