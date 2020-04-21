package com.xingpc.scp.kafka.controller;

import com.xingpc.scp.kafka.service.DemoSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: XingPc
 * @Description: ${description}
 * @Date: 2020/2/20 9:38
 * @Version: 1.0
 */
@RestController
@RequestMapping("/kafkaDemo")
public class DemoController {

    @Autowired
    private DemoSenderService kafkaSender;

    @RequestMapping("/unicast")
    public String unicast(String value) {
        kafkaSender.unicast();
        return "success";
    }

    @RequestMapping("/broadcast")
    public String broadcast(String value) {
        kafkaSender.broadcast();
        return "success";
    }

}
