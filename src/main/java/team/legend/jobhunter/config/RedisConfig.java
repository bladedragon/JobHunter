package team.legend.jobhunter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import team.legend.jobhunter.model.PreOrder;

import java.net.UnknownHostException;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, String> StringRedistemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<String, String>();
        template.setConnectionFactory(redisConnectionFactory);

        Jackson2JsonRedisSerializer ser = new Jackson2JsonRedisSerializer<String>(String.class);

        template.setDefaultSerializer(ser);
        return template;
    }

    @Bean
    public RedisTemplate<String, PreOrder> preOrderRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,PreOrder> template = new RedisTemplate<String,PreOrder>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer ser = new Jackson2JsonRedisSerializer<PreOrder>(PreOrder.class);
        template.setDefaultSerializer(ser);
        return template;
    }
}
