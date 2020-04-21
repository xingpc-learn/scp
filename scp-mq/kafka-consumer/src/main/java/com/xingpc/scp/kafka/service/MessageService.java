package com.xingpc.scp.kafka.service;


import com.xingpc.scp.kafka.model.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * @Author: XingPc
 * @Description: ${description}
 * @Date: 2020/2/20 9:36
 * @Version: 1.0
 */
@Service
public class MessageService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 消息处理业务方法
     * 
     * @param message 消息内容
     */
    public void exec(MessageModel message) {
        log.info("我收到一条信息，内容是：" + message.getMsg());
    }

}
