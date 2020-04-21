package com.xingpc.scp.activemq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: XingPc
 * @Description: ${description}
 * @Date: 2020/3/10 13:55
 * @Version: 1.0
 */
@SpringBootApplication
@Configuration
public class ActivemqConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivemqConsumerApplication.class,args);
    }

}
