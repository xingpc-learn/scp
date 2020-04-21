package com.xingpc.scp.activemq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: XingPc
 * @Description: ${description}
 * @Date: 2020/3/10 14:04
 * @Version: 1.0
 */
@SpringBootApplication
@Configuration
public class ActivemqProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivemqProducerApplication.class,args);
    }

}
