package com.angus.rabbitmq.producer.broker;

import com.angus.rabbitmq.common.convert.RabbitMessageConverter;
import com.angus.rabbitmq.common.convert.SuperRabbitMessageConverter;
import com.angus.rabbitmq.common.serializer.Serializer;
import com.angus.rabbitmq.common.serializer.SerializerFactory;
import com.angus.rabbitmq.common.serializer.impl.JacksonSerializerFactory;
import com.angus.rabbitmq.producer.api.Message;
import com.angus.rabbitmq.producer.api.MessageType;
import com.angus.rabbitmq.producer.api.exception.MessageRuntimExceptiom;
import com.angus.rabbitmq.producer.service.MessageStoreService;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author angus
 * @version 1.0.0
 * @ClassName RabbitTemplateContainer.java
 * @Description TODO
 * @createTime 2021年02月13日 11:08:00
 */
@Component
public class RabbitTemplateContainer implements RabbitTemplate.ConfirmCallback{

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private MessageStoreService messageStoreService;

    /**
     * 通过topic在缓存一个rabbitTemplate
     */
    Map<String, RabbitTemplate> templateContainer = new ConcurrentHashMap<>();

    public RabbitTemplate getTemplate(Message message){
        String topic = message.getTopic();
        String messageType = message.getMessageType();
        if (StringUtils.isEmpty(topic)){
            throw new MessageRuntimExceptiom("topic is null!");
        }
        RabbitTemplate rabbitTemplate = templateContainer.get(topic);
        if (rabbitTemplate != null) return rabbitTemplate;
        //1、进行重新创建
        RabbitTemplate newRabbitTemplate = new RabbitTemplate(connectionFactory);
        //2、普通属性进行封装
        newRabbitTemplate.setExchange(topic);
        newRabbitTemplate.setRoutingKey(message.getRoutingKey());
        newRabbitTemplate.setRetryTemplate(new RetryTemplate());
        //3、序列化进行封住装
        SerializerFactory serializerFactory = JacksonSerializerFactory.SerializerFactory;
        RabbitMessageConverter rabbitMessageConverter = new RabbitMessageConverter(serializerFactory.create(Message.class));
        SuperRabbitMessageConverter superRabbitMessageConverter = new SuperRabbitMessageConverter(rabbitMessageConverter);
        newRabbitTemplate.setMessageConverter(superRabbitMessageConverter);

        //4、是否confirm进行封装
        if (!MessageType.QUICK.equals(messageType)){
            newRabbitTemplate.setConfirmCallback(this);
        }
        templateContainer.put(topic,newRabbitTemplate);
        return newRabbitTemplate;
    }

    /**
     * @title confirm
     * @description 非quick模式下，进行ack回调方法
     * @author admin [correlationData, ack, cause]
     * @updateTime 2021/2/13 [correlationData, ack, cause] void
     * @throws
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData.getId();
        String[] split = id.split("#");
        String messageType = split[2];
        String messageId = split[0];
        //只有在confirm或者RELIANT进行confirm
        //成功的情况下
        if (ack){
            if (MessageType.RELIANT.equals(messageType)){
                messageStoreService.succuess(messageId);
            }
        }else {
            System.out.println("投递失败！");
        }
    }
}
