package com.angus.rabbitmq.producer.broker;

import com.angus.rabbitmq.producer.api.Message;
import com.angus.rabbitmq.producer.api.MessageProducer;
import com.angus.rabbitmq.producer.api.MessageType;
import com.angus.rabbitmq.producer.api.SendCallBack;

import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName ProducerClient.java
 * @Description TODO
 * @createTime 2021年02月13日 10:22:00
 */
public class ProducerClient implements MessageProducer {

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

            case MessageType.CONFIRM:
            case MessageType.RELIANT:
        }
    }

    @Override
    public void send(Message message, SendCallBack callBack) {

    }

    @Override
    public void send(List<Message> messageList) {

    }
}
