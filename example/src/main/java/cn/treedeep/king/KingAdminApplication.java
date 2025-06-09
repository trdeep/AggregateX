package cn.treedeep.king;

import cn.treedeep.king.config.EnableAggregateX;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAggregateX
@SpringBootApplication
public class KingAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(KingAdminApplication.class, args);
    }

}
