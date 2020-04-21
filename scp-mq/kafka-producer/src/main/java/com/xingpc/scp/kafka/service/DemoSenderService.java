package com.xingpc.scp.kafka.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xingpc.scp.kafka.model.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @Author: XingPc
 * @Description: ${description}
 * @Date: 2020/2/20 9:50
 * @Version: 1.0
 */
@Service
public class DemoSenderService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private Gson gson = new GsonBuilder().create();

    //发送消息方法
    public void unicast() {
        MessageModel message = new MessageModel();
        message.setId(System.currentTimeMillis());
        message.setMsg(UUID.randomUUID().toString());
        message.setSendTime(new Date());
        log.info("+++++++++++++++++++++  message = {}", gson.toJson(message));
        kafkaTemplate.send("unicast", gson.toJson(message));
    }

    public void broadcast() {
        MessageModel message = new MessageModel();
        message.setId(System.currentTimeMillis());
        message.setMsg(UUID.randomUUID().toString());
        message.setSendTime(new Date());
        log.info("+++++++++++++++++++++  message = {}", gson.toJson(message));
        kafkaTemplate.send("broadcast", gson.toJson(message));
    }

}
