package com.xingpc.scp.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: XingPc
 * @Description: ${description}
 * @Date: 2020/2/20 10:07
 * @Version: 1.0
 */
public class KafkaConsumerTest {

    /**
     * 消费者自动提交offset
     * @param
     * @Return
     */
    @Test
    @SuppressWarnings(value = "deprecated")
    public void testConsumerAutoCommitOffset() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.88.135:9092");
        // 消费者组
        props.put("group.id", "consumer01");
        // 开启自动提交
        props.put("enable.auto.commit", "true");
        // 自动提交延时
        props.put("auto.commit.interval.ms_config", "1000");

        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("first"));
        while (true) {
            ConsumerRecords<String, String> records =
                    consumer.poll(100);
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }
    }

    /**
     * 消费者同步提交offset
     * @param
     * @Return
     */
    @Test
    @SuppressWarnings(value = "deprecated")
    public void testConsumerAutoCommitOffsetSync() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.88.135:9092");
        props.put("group.id", "consumer01");
        props.put("enable.auto.commit", "false");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("first"));
        while (true) {
            ConsumerRecords<String, String> records =
                    consumer.poll(100);
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            //同步提交，当前线程会阻塞直到 offset 提交成功
            consumer.commitSync();
        }
    }

    /**
     * 消费者异步提交offset
     * @param
     * @Return
     */
    @Test
    @SuppressWarnings(value = "deprecated")
    public void testConsumerAutoCommitOffsetAsync() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.88.135:9092");
        props.put("group.id", "consumer01");
        // 关闭自动提交
        props.put("enable.auto.commit", "false");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("first"));
        while (true) {
            ConsumerRecords<String, String> records =
                    consumer.poll(100);
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            //异步提交offset
            consumer.commitAsync(new OffsetCommitCallback() {
                @Override
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                        if (exception != null) {
                            System.err.println("Commit  failed  for" + offsets);
                        }
                }
            });
        }
    }

    /**
     * 消费者新加或退出时，重新分配分区
     * @param
     * @Return
     */
    @Test
    @SuppressWarnings(value = "deprecated")
    public void testConsumerAutoCommitOffsetCustomOffset() {
        //创建配置信息
        Properties props = new Properties();
        //Kafka 集群
        props.put("bootstrap.servers", "192.168.88.135:9092");
        //消费者组，只要 group.id 相同，就属于同一个消费者组
        props.put("group.id", "consumer01");
        //关闭自动提交 offset
        props.put("enable.auto.commit", "false");
        //Key 和 Value 的反序列化类
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //创建一个消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        //消费者订阅主题
        consumer.subscribe(Arrays.asList("first"), new ConsumerRebalanceListener() {
            //该方法会在 Rebalance 之前调用
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
//                commitOffset(currentOffset);
            }
            //该方法会在 Rebalance 之后调用
            @Override
            public void
            onPartitionsAssigned(Collection<TopicPartition> partitions) {
//                currentOffset.clear();
                for (TopicPartition partition : partitions) {
                    //定位到最近提交的 offset 位置继续消费
                    consumer.seek(partition, getOffset(partition));
                }
            }
        });
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);//消费者拉取数据
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
//                currentOffset.put(new TopicPartition(record.topic(), record.partition()), record.offset());
            }
            // 异步提交
//          commitOffset(currentOffset);
        }
    }

    //获取某分区的最新 offset
    private static long getOffset(TopicPartition partition) {
        return 0;
    }

    //提交该消费者所有分区的 offset
    private static void commitOffset(Map<TopicPartition, Long> currentOffset) {
    }

}

