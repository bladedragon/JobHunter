package team.legend.jobhunter.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TimeLogAdvice {

    @Pointcut("execution(public team.legend.jobhunter.controller.*.*(..))")
    public void log(){
    }

    @Around("log()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long time1 = System.currentTimeMillis();
        Object obj = pjp.proceed();
        log.info("request time:{}ms", System.currentTimeMillis() - time1);
        return obj;
    }
}
