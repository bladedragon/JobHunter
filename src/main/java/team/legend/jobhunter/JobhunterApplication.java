package team.legend.jobhunter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan({"import team.legend.jobhunter.dao"})
@SpringBootApplication
public class JobhunterApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobhunterApplication.class, args);
    }

}
