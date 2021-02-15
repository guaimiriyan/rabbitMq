package com.angus.rabbitmq.producer.broker;


import com.angus.rabbitmq.producer.api.Message;
import com.angus.rabbitmq.producer.api.MessageType;
import com.angus.rabbitmq.producer.constant.BrokerMessageConst;
import com.angus.rabbitmq.producer.constant.BrokerMessageStatus;
import com.angus.rabbitmq.producer.entity.BrokerMessage;
import com.angus.rabbitmq.producer.service.MessageStoreService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName RabbitBrokerImpl.java
 * @Description TODO
 * @createTime 2021年02月13日 10:37:00
 */
@Service
public class RabbitBrokerImpl implements RabbitBroker{

    @Autowired
    RabbitTemplateContainer rabbitTemplateContainer;

    @Autowired
    MessageStoreService messageStoreService;


    @Override
    public void sendQuick(Message message) {
        message.setMessageType(MessageType.QUICK);
        sendKernel(message);
    }


    @Override
    public void sendConfirm(Message message) {
        message.setMessageType(MessageType.CONFIRM);
        sendKernel(message);

    }

    @Override
    public void sendRapid(Message message) {
        //1、首先进行meaassge保存
        message.setMessageType(MessageType.RELIANT);
        BrokerMessage bm = messageStoreService.selectByMessageId(message.getMessageId());
        if(bm == null) {
            //1. 把数据库的消息发送日志先记录好
            Date now = new Date();
            BrokerMessage brokerMessage = new BrokerMessage();
            brokerMessage.setMessageId(message.getMessageId());
            brokerMessage.setStatus(BrokerMessageStatus.SENDING.getCode());
            //tryCount 在最开始发送的时候不需要进行设置
            brokerMessage.setNextRetry(DateUtils.addMinutes(now, BrokerMessageConst.TIMEOUT));
            brokerMessage.setCreateTime(now);
            brokerMessage.setUpdateTime(now);
            brokerMessage.setMessage(message);
            messageStoreService.insert(brokerMessage);
        }
        //2、调用sendKernel
        sendKernel(message);

    }

    @Override
    public void sendBatchMessage() {
        List<Message> clear = MessageHolder.clear();
        clear.forEach(message -> {
            AsynSendKernel(message);
        });
    }


    /**
     * @title sendKernel
     * @description 进行消息发送的核心方法
     * @author admin [message]
     * @updateTime 2021/2/13 [message] void
     * @throws
     */
    private void sendKernel(Message message) {
        //1、创建独一无二的CorrelationData创建
        CorrelationData correlationData = new CorrelationData(String.format("%s#%s#%s",
                message.getMessageId(),
                System.currentTimeMillis(),
                message.getMessageType()));
        //2、获取所需要的参数
        String topic = message.getTopic();
        String routingKey = message.getRoutingKey();
        //3、此处可以进行优化，将rabbitTemplate进行池化
        RabbitTemplate template = rabbitTemplateContainer.getTemplate(message);
        template.convertAndSend(topic,routingKey,message,correlationData);
    }

    /**
     * @title AsynSendKernel
     * @description 异步消息发送
     * @author admin [message]
     * @updateTime 2021/2/13 [message] void
     * @throws
     */
    private  void AsynSendKernel(Message message){
        AsynBaseQueue.submit(new Runnable() {
            @Override
            public void run() {
                //1、创建独一无二的CorrelationData创建
                CorrelationData correlationData = new CorrelationData(String.format("%s#%s#%s",
                        message.getMessageId(),
                        System.currentTimeMillis(),
                        message.getMessageType()));
                //2、获取所需要的参数
                String topic = message.getTopic();
                String routingKey = message.getRoutingKey();
                //3、此处可以进行优化，将rabbitTemplate进行池化
                RabbitTemplate template = rabbitTemplateContainer.getTemplate(message);
                template.convertAndSend(topic,routingKey,message,correlationData);
            }
        });
    }


}
