package com.xingpc.scp.producer;

import org.apache.kafka.clients.producer.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @Author: XingPc
 * @Description: 生产者发送消息
 * @Date: 2020/2/20 9:08
 * @Version: 1.0
 */
public class KafkaProducerTest {

    /**
     * 不带回调函数
     * @param
     * @Return
     */
    @Test
    public void testProduceWithoutCallable() {
        Properties props = new Properties();
        //kafka 集群，broker-list
        props.put("bootstrap.servers", "192.168.88.135:9092");
        props.put("acks", "all");
        //重试次数
        props.put("retries", 1);
        //批次大小
        props.put("batch.size", 16384);
        //等待时间
        props.put("linger.ms", 1);
        //RecordAccumulator 缓冲区大小
        props.put("buffer.memory", 33554432);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
            producer.send(new ProducerRecord<String, String>("first",
                    Integer.toString(i), Integer.toString(i)));
        }
        producer.close();
    }

    /**
     * 带回调函数api
     * @param
     * @Return
     */
    @Test
    public void testProduceWithCallable() {
        Properties props = new Properties();
        //kafka 集群，broker-list
        props.put("bootstrap.servers", "192.168.88.135:9092");
        props.put("acks", "all");
        //重试次数
        props.put("retries", 1);
        //批次大小
        props.put("batch.size", 16384);
        //等待时间
        props.put("linger.ms", 1);
        //RecordAccumulator 缓冲区大小
        props.put("buffer.memory", 33554432);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
            producer.send(new ProducerRecord<String, String>("first",
                    Integer.toString(i), Integer.toString(i)), new Callback() {
                // 回调函数，该方法会在 Producer 收到 ack 时调用，为异步调用
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null) {
                        System.out.println("success->" + metadata.offset());
                    } else {
                        exception.printStackTrace();
                    }
                }
            });
        }
        producer.close();
    }

    /**
     * 同步发送数据
     * @param
     * @Return
     */
    @Test
    public void testProduceSerial() throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        //kafka 集群，broker-list
        props.put("bootstrap.servers", "192.168.88.135:9092");
        props.put("acks", "all");
        //重试次数
        props.put("retries", 1);
        //批次大小
        props.put("batch.size", 16384);
        //等待时间
        props.put("linger.ms", 1);
        //RecordAccumulator 缓冲区大小
        props.put("buffer.memory", 33554432);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
            // 同步发送数据，一条消息发送之后，会阻塞当前线程。只需在调用 Future 对象的 get 方发即可
            producer.send(new ProducerRecord<String, String>("first",
                    Integer.toString(i), Integer.toString(i))).get();
        }
        producer.close();
    }

    /**
     * 生产消息使用过滤器链
     * @param
     * @Return
     */
    @Test
    public void testProduceWithInterceptor() throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        //kafka 集群，broker-list
        props.put("bootstrap.servers", "192.168.88.135:9092");
        props.put("acks", "all");
        //重试次数
        props.put("retries", 3);
        //批次大小
        props.put("batch.size", 16384);
        //等待时间
        props.put("linger.ms", 1);
        //RecordAccumulator 缓冲区大小
        props.put("buffer.memory", 33554432);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        // 构建拦截链
        List<String> interceptors = new ArrayList<>();
        interceptors.add("com.xingpc.scp.kafka.interceptor.CounterInterceptor");
        interceptors.add("com.xingpc.scp.kafka.interceptor.TimeInterceptor");
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptors);
        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
            // 同步发送数据
            producer.send(new ProducerRecord<String, String>("first",
                    Integer.toString(i), "message"+ i));
        }
        // 一定要关闭 producer，这样才会调用 interceptor 的 close 方法
        producer.close();
    }

}
