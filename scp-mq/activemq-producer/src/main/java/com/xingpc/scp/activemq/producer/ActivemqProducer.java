package com.xingpc.scp.activemq.producer;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Author: XingPc
 * @Description: ${description}
 * @Date: 2020/3/10 13:47
 * @Version: 1.0
 */
@Component
public class ActivemqProducer {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    /**
     * 发送字符串消息队列
     *
     * @param queueName 队列名称
     * @param message   字符串
     */
    public void sendStringQueue(String queueName, String message) {
        this.jmsMessagingTemplate.convertAndSend(new ActiveMQQueue(queueName), message);
    }

}
