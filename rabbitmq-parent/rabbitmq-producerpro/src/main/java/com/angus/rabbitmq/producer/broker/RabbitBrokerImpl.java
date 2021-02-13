package com.angus.rabbitmq.producer.broker;


import com.angus.rabbitmq.producer.api.Message;
import com.angus.rabbitmq.producer.api.MessageType;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName RabbitBrokerImpl.java
 * @Description TODO
 * @createTime 2021年02月13日 10:37:00
 */
public class RabbitBrokerImpl implements RabbitBroker{

    @Autowired
    RabbitTemplateContainer rabbitTemplateContainer;


    @Override
    public void sendQuick(Message message) {
        message.setMessageType(MessageType.QUICK);
        sendKernel(message);
    }


    @Override
    public void sendConfirm(Message message) {

    }

    @Override
    public void sendRapid(Message message) {

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
