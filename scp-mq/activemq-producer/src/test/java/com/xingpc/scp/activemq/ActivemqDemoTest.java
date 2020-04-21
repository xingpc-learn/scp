package com.xingpc.scp.activemq;

import com.xingpc.scp.activemq.producer.ActivemqProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author: XingPc
 * @Description: ${description}
 * @Date: 2020/3/10 13:51
 * @Version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ActivemqDemoTest {

    @Resource
    private ActivemqProducer activemqProducer;

    @Test
    public void testStringQueue() {
        for (int i = 1; i <= 100; i++) {
            System.out.println("第" + i + "次发送字符串队列消息");
            activemqProducer.sendStringQueue("stringQueue", "消息：" + i);
        }
    }

}
