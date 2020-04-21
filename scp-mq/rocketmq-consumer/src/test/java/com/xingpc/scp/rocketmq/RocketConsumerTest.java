package com.xingpc.scp.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author: XingPc
 * @Description: rocketmq 消费者各种模式
 * @Date: 2020/2/19 10:18
 * @Version: 1.0
 */
public class RocketConsumerTest {

    /**
     * 消费消息，负载均衡模式
     *
     * @param
     * @Return
     */
    @Test
    public void testLoadBalance() throws Exception {
        // 实例化消息生产者,指定组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer_group1");
        // 指定Namesrv地址信息.
        consumer.setNamesrvAddr("192.168.88.135:9876;192.168.88.133:9876");
        // 订阅Topic
        consumer.subscribe("TopicTest", "*");
        //负载均衡模式消费
        consumer.setMessageModel(MessageModel.CLUSTERING);
        // 注册回调函数，处理消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s %n",
                        Thread.currentThread().getName(), msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //启动消息者
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }

    /**
     * 消费消息，广播模式
     * @param 
     * @Return 
     */
    @Test
    public void testBroad() throws Exception {
        // 实例化消息生产者,指定组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer_group2");
        // 指定Namesrv地址信息.
        consumer.setNamesrvAddr("192.168.88.135:9876;192.168.88.133:9876");
        // 订阅Topic
        consumer.subscribe("TopicTest", "*");
        //广播模式消费
        consumer.setMessageModel(MessageModel.BROADCASTING);
        // 注册回调函数，处理消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s %n",
                        Thread.currentThread().getName(), msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //启动消息者
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }

    /**
     * 顺序消息消费，带事务方式（应用可控制Offset什么时候提交）
     * @param
     * @Return
     */
    @Test
    public void testOrderedConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new
                DefaultMQPushConsumer("consumer_group3");
        consumer.setNamesrvAddr("192.168.88.135:9876;192.168.88.133:9876");
        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        consumer.subscribe("TopicTest", "TagA || TagC || TagD");

        consumer.registerMessageListener(new MessageListenerOrderly() {

            Random random = new Random();

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                context.setAutoCommit(true);
                for (MessageExt msg : msgs) {
                    // 可以看到每个queue有唯一的consume线程来消费, 订单对每个queue(分区)有序
                    System.out.println("consumeThread=" + Thread.currentThread().getName() + "queueId=" + msg.getQueueId() + ", content:" + new String(msg.getBody()));
                }

                try {
                    //模拟业务逻辑处理中...
                    TimeUnit.SECONDS.sleep(random.nextInt(10));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        consumer.start();

        System.out.println("Consumer Started.");
    }

    /**
     * 延时消息
     * @param
     * @Return 
     */
    @Test
    public void testScheduleMessageConsumer() throws Exception {
        // 实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ExampleConsumer");
        //2.指定Nameserver地址
        consumer.setNamesrvAddr("192.168.88.135:9876;192.168.88.133:9876");
        // 订阅Topics
        consumer.subscribe("TestTopic", "*");
        // 注册消息监听者
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages, ConsumeConcurrentlyContext context) {
                for (MessageExt message : messages) {
                    // Print approximate delay time period
                    System.out.println("Receive message[msgId=" + message.getMsgId() + "] " + (System.currentTimeMillis() - message.getStoreTimestamp()) + "ms later");
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // 启动消费者
        consumer.start();
    }

    /**
     * 消息过滤 sql
     * @param
     * @Return
     */
    @Test
    public void testFilterMessageConsumer() throws Exception {
        //1.创建消费者Consumer，制定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
        //2.指定Nameserver地址
        consumer.setNamesrvAddr("192.168.88.135:9876;192.168.88.133:9876");
        //3.订阅主题Topic和Tag
        consumer.subscribe("FilterSQLTopic", MessageSelector.bySql("i>5"));

        //4.设置回调函数，处理消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            //接受消息内容
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println("consumeThread=" + Thread.currentThread().getName() + "," + new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //5.启动消费者consumer
        consumer.start();
        System.out.println("消费者启动");
    }

    /**
     * 消息过滤 tag
     * @param
     * @Return
     */
    @Test
    public void testFilterMessageConsumer2() throws Exception {
        //1.创建消费者Consumer，制定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
        //2.指定Nameserver地址
        consumer.setNamesrvAddr("192.168.88.135:9876;192.168.88.133:9876");
        //3.订阅主题Topic和Tag
        consumer.subscribe("FilterTagTopic", "Tag1 || Tag2 ");

        //4.设置回调函数，处理消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            //接受消息内容
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println("consumeThread=" + Thread.currentThread().getName() + "," + new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //5.启动消费者consumer
        consumer.start();
        System.out.println("消费者启动");
    }

    /**
     * 批量消息消费
     * @param
     * @Return
     */
    @Test
    public void testBatchConsumer() throws Exception {
        //1.创建消费者Consumer，制定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
        //2.指定Nameserver地址
        consumer.setNamesrvAddr("192.168.88.135:9876;192.168.88.133:9876");
        //3.订阅主题Topic和Tag
        consumer.subscribe("BatchTopic", "*");

        //4.设置回调函数，处理消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            //接受消息内容
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println("consumeThread=" + Thread.currentThread().getName() + "," + new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //5.启动消费者consumer
        consumer.start();

        System.out.println("消费者启动");
    }
    
    /**
     * 消息消费事务
     * @param 
     * @Return 
     */
    @Test
    public void testTransactionConsumer() throws Exception {
        //1.创建消费者Consumer，制定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer_group4");
        //2.指定Nameserver地址
        consumer.setNamesrvAddr("192.168.88.135:9876;192.168.88.133:9876");
        //3.订阅主题Topic和Tag
        consumer.subscribe("TransactionTopic", "*");
        //4.设置回调函数，处理消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            //接受消息内容
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println("consumeThread=" + Thread.currentThread().getName() + "," + new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //5.启动消费者consumer
        consumer.start();
        System.out.println("消费者启动");
    }

}
