package com.angus.rabbitmq.producer.broker;

import com.angus.rabbitmq.producer.api.Message;
import com.angus.rabbitmq.producer.api.MessageProducer;
import com.angus.rabbitmq.producer.api.MessageType;
import com.angus.rabbitmq.producer.api.SendCallBack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName ProducerClient.java
 * @Description TODO
 * @createTime 2021年02月13日 10:22:00
 */
@Component
public class ProducerClient implements MessageProducer {

    @Autowired
    RabbitBroker rabbitBroker;
    /**
     * @title send
     * @description 快速推送可分为三类
     * @author admin [message]
     * @updateTime 2021/2/13 [message] void
     * @throws
     */
    @Override
    public void send(Message message) {
        //1、首先获取消息的类别
        String messageType = message.getMessageType();
        switch (messageType){
            case MessageType.QUICK:
                rabbitBroker.sendQuick(message);
            case MessageType.CONFIRM:
                rabbitBroker.sendConfirm(message);
            case MessageType.RELIANT:
                rabbitBroker.sendRapid(message);
        }
    }

    @Override
    public void send(Message message, SendCallBack callBack) {

    }

    @Override
    public void send(List<Message> messageList) {
        messageList.forEach( message -> {
            message.setMessageType(MessageType.QUICK);
            MessageHolder.addMessage(message);
        });
        rabbitBroker.sendBatchMessage();
    }
}
