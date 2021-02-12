package com.angus.rabbitmq.api;

import com.angus.rabbitmq.api.exception.MessageRuntimExceptiom;
import sun.misc.Contended;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName MessageBuilder.java
 * @Description 创建Message的类
 * @createTime 2021年02月13日 01:40:00
 */
public class MessageBuilder {

    private String messageId;
    private String topic;
    private String routingKey = "";
    private Map<String, Object> attributes = new HashMap<String, Object>();
    private int delayMills;



    /**
     * 创建构造器
     */

    public MessageBuilder() {
    }

    public static MessageBuilder creat(){
        return new MessageBuilder();
    }

    /**
     * 需要使用链式的方式进行Message的创建
     */

    public MessageBuilder setMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }
    public MessageBuilder setTopic(String topic) {
        this.topic = topic;
        return this;
    }
    public MessageBuilder setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
        return this;
    }

    public MessageBuilder setAttributes(Map<String,Object> attributes) {
        this.attributes = attributes;
        return this;
    }

    public MessageBuilder setAttributes(String key,Object value) {
        this.attributes.put(key,value);
        return this;
    }

    public MessageBuilder setDelayMills(int delayMills) {
        this.delayMills = delayMills;
        return this;
    }

    public  Message build(){
        if (messageId == null){
            messageId = UUID.randomUUID().toString();
        }
        //当broker为空时，则抛出异常
        if (topic == null){
            throw new MessageRuntimExceptiom("Message Topic is null!");
        }
        return new Message(messageId,topic,routingKey,attributes,delayMills);
    }


}
