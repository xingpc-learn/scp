package com.xingpc.scp.activemq.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @Author: XingPc
 * @Description: ${description}
 * @Date: 2020/3/10 13:47
 * @Version: 1.0
 */
@Component
public class ActivemqConsumer {

    @JmsListener(destination = "stringQueue", containerFactory = "jmsListenerContainerQueue")
    public void receiveStringQueue(String msg) {
        System.out.println("接收到消息...." + msg);
    }

}
