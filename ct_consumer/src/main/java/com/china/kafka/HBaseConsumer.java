package com.china.kafka;



import com.china.dao.HBaseDAO;
import com.china.utils.PropertiesUtil;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * 读取kafka中的缓存消息到控制台测试
 */
public class HBaseConsumer {
    public static void main(String[] args) {

        // 创建配置对象
        ConsumerConfig consumerConfig = new ConsumerConfig(PropertiesUtil.properties);

        // 得到当前kafka集群的消费主题，在配置文件中的key=values
        String callLogTopic = PropertiesUtil.getproperties("topic");

        // 订阅主题，开始消费
        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);

        //创建一个map集合
        Map<String, Integer> topicMap = new HashMap<>();

        //把消费的主题加到map中
        topicMap.put(callLogTopic,1);

        //实例化解码器   VerifiableProperties属性的键值对，
        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());

        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());

        //通过消费者连接器得到一个kafka集群的stream对象
        Map<String ,List<KafkaStream<String,String >>> consumerMap

                = consumerConnector.createMessageStreams(topicMap,keyDecoder,valueDecoder);

        //get(callLogTopic) 获取 List<KafkaStream<String,String >>
        //get(0) 获取  KafkaStream<String,String >
        KafkaStream<String ,String> stream = consumerMap.get(callLogTopic).get(0);

        ConsumerIterator<String, String> it = stream.iterator();

        HBaseDAO hBaseDAO = new HBaseDAO();

      // List<String> msgList = new ArrayList<>();


        while(it.hasNext()){

            // 将消息实时写入到Hbase中
            String msg = it.next().message();

           //  msgList.add(msg);
            System.out.println(msg);

           // hBaseDAO.put(msgList.get());
            hBaseDAO.put(msg);
            //使用list集合一次加入一批来优化？？？？？
        }

    }

}