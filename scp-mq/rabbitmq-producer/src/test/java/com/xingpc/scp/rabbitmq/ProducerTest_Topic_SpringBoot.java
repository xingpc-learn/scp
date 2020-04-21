package com.xingpc.scp.rabbitmq;

import com.xingpc.scp.rabbitmq.config.RabbitmqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: XingPc
 * @Description: ${description}
 * @Date: 2020/2/21 11:28
 * @Version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProducerTest_Topic_SpringBoot {

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * RabbitMQ存在名字重复交换机报错
     * */
    @Test
    public void testRabbitTemplate(){
        //定义发送信息
        String message="send to user by email";
        //参数：交换机名称，routingkey，message
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPIC_INFORM_SPRING,RabbitmqConfig.ROUTING_KEY_EMAIL ,message);

    }

}
