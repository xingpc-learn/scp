package com.xingpc.scp.rocketmq;

import com.xingpc.scp.rocketmq.model.OrderStep;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: XingPc
 * @Description: rocketmq 生产者各种模式
 * @Date: 2020/2/19 10:18
 * @Version: 1.0
 */
public class RocketProducerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketProducerTest.class);

    /**
     * 发送同步消息
     * @param
     * @Return 
     */
    @Test
    public void testSyncProducer() throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("producer_group_test_sync");
        // 设置NameServer的地址
        producer.setNamesrvAddr("192.168.88.135:9876;192.168.88.133:9876");
        // 启动Producer实例
        producer.start();
        for (int i = 0; i < 100; i++) {
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message("TopicTest" /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            // 发送消息到一个Broker
            SendResult sendResult = producer.send(msg,10000);
            // 通过sendResult返回消息是否成功送达
            System.out.printf("%s%n", sendResult);
        }
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }
    
    /**
     * 发送异步消息
     * @param 
     * @Return 
     */
    @Test
    public void  testAsyncProducer() throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("producer_group_test_async");
        // 设置NameServer的地址
        producer.setNamesrvAddr("192.168.88.135:9876;192.168.88.133:9876");
        // 启动Producer实例
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        for (int i = 0; i < 100; i++) {
            final int index = i;
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message("TopicTest2",
                    "TagA",
                    "OrderID188",
                    "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
            // SendCallback接收异步返回结果的回调
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.printf("%-10d OK %s %n", index,
                            sendResult.getMsgId());
                }
                @Override
                public void onException(Throwable e) {
                    System.out.printf("%-10d Exception %s %n", index, e);
                    e.printStackTrace();
                }
            });
        }
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }

    /**
     * 测试单向发送消息
     * @param
     * @Return
     */
    @Test
    public void testOneWayProducer() throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("producer_group_test_oneway");
        // 设置NameServer的地址
        producer.setNamesrvAddr("192.168.88.135:9876;192.168.88.133:9876");
        // 启动Producer实例
        producer.start();
        for (int i = 0; i < 100; i++) {
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message("TopicTest3" /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            // 发送单向消息，没有任何返回结果
            producer.sendOneway(msg);

        }
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }

    /**
     * 顺序生产消息
     * @param
     * @Return 
     */
    @Test
    public void testOrderedProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("producer_group_test_ordered");

        producer.setNamesrvAddr("192.168.88.135:9876;192.168.88.133:9876");

        producer.start();

        String[] tags = new String[]{"TagA", "TagC", "TagD"};

        // 订单列表
        List<OrderStep> orderList = buildOrders();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        for (int i = 0; i < 10; i++) {
            // 加个时间前缀
            String body = dateStr + " Hello RocketMQ " + orderList.get(i);
            Message msg = new Message("TopicTest", tags[i % tags.length], "KEY" + i, body.getBytes());

            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Long id = (Long) arg;  //根据订单id选择发送queue
                    long index = id % mqs.size();
                    return mqs.get((int) index);
                }
            }, orderList.get(i).getOrderId());//订单id

            System.out.println(String.format("SendResult status:%s, queueId:%d, body:%s",
                    sendResult.getSendStatus(),
                    sendResult.getMessageQueue().getQueueId(),
                    body));
        }

        producer.shutdown();
    }

    /**
     * 发送延时消息
     * @param
     * @Return
     */
    @Test
    public void testScheduleMessageProducer() throws Exception {
        // 实例化一个生产者来产生延时消息
        DefaultMQProducer producer = new DefaultMQProducer("ExampleProducerGroup");
        //2.指定Nameserver地址
        producer.setNamesrvAddr("192.168.88.135:9876;192.168.88.133:9876");
        // 启动生产者
        producer.start();
        int totalMessagesToSend = 100;
        for (int i = 0; i < totalMessagesToSend; i++) {
            Message message = new Message("TestTopic", ("Hello scheduled message " + i).getBytes());
            // 设置延时等级3,这个消息将在10s之后发送(现在只支持固定的几个时间,详看delayTimeLevel)
            message.setDelayTimeLevel(3);
            // 发送消息
            producer.send(message);
        }
        // 关闭生产者
        producer.shutdown();
    }

    /**
     * 消息过滤 sql
     * @param
     * @Return
     */
    @Test
    public void testFilterMessageProducer() throws Exception {
        //1.创建消息生产者producer，并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //2.指定Nameserver地址
        producer.setNamesrvAddr("192.168.88.135:9876;192.168.88.133:9876");
        //3.启动producer
        producer.start();

        for (int i = 0; i < 10; i++) {
            //4.创建消息对象，指定主题Topic、Tag和消息体
            /**
             * 参数一：消息主题Topic
             * 参数二：消息Tag
             * 参数三：消息内容
             */
            Message msg = new Message("FilterSQLTopic", "Tag1", ("Hello World" + i).getBytes());

            msg.putUserProperty("i", String.valueOf(i));

            //5.发送消息
            SendResult result = producer.send(msg);
            //发送状态
            SendStatus status = result.getSendStatus();

            System.out.println("发送结果:" + result);

            //线程睡1秒
            TimeUnit.SECONDS.sleep(2);
        }

        //6.关闭生产者producer
        producer.shutdown();
    }

    /**
     * 消息过滤 tag
     * @param
     * @Return
     */
    @Test
    public void testFilterMessageProducer2() throws Exception {
        //1.创建消息生产者producer，并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //2.指定Nameserver地址
        producer.setNamesrvAddr("192.168.88.135:9876;192.168.88.133:9876");
        //3.启动producer
        producer.start();

        for (int i = 0; i < 3; i++) {
            //4.创建消息对象，指定主题Topic、Tag和消息体
            /**
             * 参数一：消息主题Topic
             * 参数二：消息Tag
             * 参数三：消息内容
             */
            Message msg = new Message("FilterTagTopic", "Tag2", ("Hello World" + i).getBytes());
            //5.发送消息
            SendResult result = producer.send(msg);
            //发送状态
            SendStatus status = result.getSendStatus();

            System.out.println("发送结果:" + result);

            //线程睡1秒
            TimeUnit.SECONDS.sleep(1);
        }

        //6.关闭生产者producer
        producer.shutdown();
    }
    
    /**
     * 发送批量消息
     * @param 
     * @Return 
     */
    @Test
    public void testBatchProducer() throws Exception {
        //1.创建消息生产者producer，并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //2.指定Nameserver地址
        producer.setNamesrvAddr("192.168.88.135:9876;192.168.88.133:9876");
        //3.启动producer
        producer.start();

        List<Message> msgs = new ArrayList<Message>();

        //4.创建消息对象，指定主题Topic、Tag和消息体
        /**
         * 参数一：消息主题Topic
         * 参数二：消息Tag
         * 参数三：消息内容
         */
        Message msg1 = new Message("BatchTopic", "Tag1", ("Hello World" + 1).getBytes());
        Message msg2 = new Message("BatchTopic", "Tag1", ("Hello World" + 2).getBytes());
        Message msg3 = new Message("BatchTopic", "Tag1", ("Hello World" + 3).getBytes());

        msgs.add(msg1);
        msgs.add(msg2);
        msgs.add(msg3);

        //5.发送消息
        SendResult result = producer.send(msgs);
        //发送状态
        SendStatus status = result.getSendStatus();

        System.out.println("发送结果:" + result);

        //线程睡1秒
        TimeUnit.SECONDS.sleep(1);


        //6.关闭生产者producer
        producer.shutdown();
    }

    /**
     * 事务性生产者
     * @param
     * @Return 
     */
    @Test
    public void testTransactionProducer() throws Exception {
        //1.创建消息生产者producer，并指定生产者组名
        TransactionMQProducer producer = new TransactionMQProducer("group5");
        //2.指定Nameserver地址
        producer.setNamesrvAddr("192.168.88.135:9876;192.168.88.133:9876");

        //添加事务监听器
        producer.setTransactionListener(new TransactionListener() {
            /**
             * 在该方法中执行本地事务
             * @param msg
             * @param arg
             * @return
             */
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                if (StringUtils.equals("TAGA", msg.getTags())) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                } else if (StringUtils.equals("TAGB", msg.getTags())) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                } else if (StringUtils.equals("TAGC", msg.getTags())) {
                    return LocalTransactionState.UNKNOW;
                }
                return LocalTransactionState.UNKNOW;
            }

            /**
             * 该方法时MQ进行消息事务状态回查
             * @param msg
             * @return
             */
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                System.out.println("消息的Tag:" + msg.getTags());
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });

        //3.启动producer
        producer.start();

        String[] tags = {"TAGA", "TAGB", "TAGC"};

        for (int i = 0; i < 3; i++) {
            //4.创建消息对象，指定主题Topic、Tag和消息体
            /**
             * 参数一：消息主题Topic
             * 参数二：消息Tag
             * 参数三：消息内容
             */
            Message msg = new Message("TransactionTopic", tags[i], ("Hello World" + i).getBytes());
            //5.发送消息
            SendResult result = producer.sendMessageInTransaction(msg, null);
            //发送状态
            SendStatus status = result.getSendStatus();

            System.out.println("发送结果:" + result);

            //线程睡1秒
            TimeUnit.SECONDS.sleep(2);
        }

        //6.关闭生产者producer
        //producer.shutdown();
    }

    /**
     * 生成模拟订单数据
     */
    private List<OrderStep> buildOrders() {
        List<OrderStep> orderList = new ArrayList<OrderStep>();

        OrderStep orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("推送");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        return orderList;
    }

}
