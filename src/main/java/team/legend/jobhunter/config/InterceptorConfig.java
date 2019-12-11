package team.legend.jobhunter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.legend.jobhunter.intercepter.JwtIntercepter;

@Component
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private JwtIntercepter jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截所有的controller
            registry.addInterceptor(jwtInterceptor).excludePathPatterns("/wx/**").
                    excludePathPatterns("/getInfo/**").excludePathPatterns("/code");
    }


}
