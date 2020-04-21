package com.xingpc.scp.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: XingPc
 * @Description: ${description}
 * @Date: 2020/2/20 9:36
 * @Version: 1.0
 */
@SpringBootApplication
@Configuration
public class KafkaConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumerApplication.class,args);
    }

}
