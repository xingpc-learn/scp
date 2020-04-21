package com.xingpc.scp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: XingPc
 * @Description: ${description}
 * @Date: 2020/2/20 9:30
 * @Version: 1.0
 */
@SpringBootApplication
@EnableScheduling
@Configuration
public class KafkaProduceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaProduceApplication.class,args);
    }

}
