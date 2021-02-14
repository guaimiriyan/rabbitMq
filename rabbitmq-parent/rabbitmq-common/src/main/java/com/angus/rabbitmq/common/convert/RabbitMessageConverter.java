package com.angus.rabbitmq.common.convert;

import com.angus.rabbitmq.common.serializer.Serializer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.messaging.MessageHeaders;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName RabbitMessageConvert.java
 * @Description 该方法为spring Message ，转为 angus.Message
 * @createTime 2021年02月14日 15:50:00
 */
public class RabbitMessageConverter implements MessageConverter {


    private Serializer serializer;

    public RabbitMessageConverter(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    /**
     * @title toMessage
     * @description
     * @author admin [object, messageProperties]
     * @updateTime 2021/2/14 [object, messageProperties] org.springframework.amqp.core.Message
     * @throws 将angus.message转为amqp.meaasge
     */
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {

        return null;
    }


    @Override
    /**
     * @title fromMessage
     * @description
     * @author admin [message]
     * @updateTime 2021/2/14 [message] java.lang.Object
     * @throws 将amqp.message转为angus.message
     */
    public Object fromMessage(Message message) throws MessageConversionException {
        return null;
    }
}
