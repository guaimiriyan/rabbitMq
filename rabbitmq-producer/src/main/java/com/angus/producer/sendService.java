package com.angus.producer;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Correlation;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/*
    该服务为简单的rabbitsmq简单的发送端服务
 */
@Component
public class sendService {
    //注入rabbitTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 	这里就是确认消息的回调监听接口，用于确认消息是否被broker所收到
     */

    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        /**
         * 	@param CorrelationData 作为一个唯一的标识
         * 	@param ack broker 是否落盘成功
         * 	@param cause 失败的一些异常信息
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.err.println("消息ACK结果:" + ack + ", correlationData: " + correlationData.getId());
        }
    };



    /**
     * @title send
     * @description
     * @author angus
     * @params [message, param]
     * @updateTime 2021/2/13 [message, param] void
     */
    public void send(Object message, Map<String,Object> param){
        //1、首先进行消息的封装
        MessageHeaders messageHeaders = new MessageHeaders(param);
        Message msg = MessageBuilder.createMessage(message, messageHeaders);

        rabbitTemplate.setConfirmCallback(confirmCallback);
        //2、进行回调函数的配置
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor(){

            @Override
            public org.springframework.amqp.core.Message postProcessMessage(org.springframework.amqp.core.Message message) throws AmqpException {
               //在此处进行参数的打印
                System.out.println("MessagePostProcessor do something"+message);
                return message;
            }
        };
        //3、传入全局唯一uuid，以便于获取到全局的ask返回关联数据
        CorrelationData rrelationData = new CorrelationData(String.valueOf(UUID.randomUUID()));
        //4、进行消息的发送
        //5、如果未启动客户端，则其中没有exchange和queue会失败
        rabbitTemplate.convertAndSend("exchange-angus","angus.*",msg,messagePostProcessor,rrelationData);

    }

}
