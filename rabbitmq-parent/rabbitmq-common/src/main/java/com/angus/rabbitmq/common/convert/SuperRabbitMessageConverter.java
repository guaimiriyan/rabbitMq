package com.angus.rabbitmq.common.convert;

import com.google.common.base.Preconditions;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName SuperRabbitMessageConverter.java
 * @Description 针对将RabbitMessageConverter进行增强
 * @createTime 2021年02月14日 15:59:00
 */
public class SuperRabbitMessageConverter implements MessageConverter {

    private RabbitMessageConverter rabbitMessageConverter;

    public SuperRabbitMessageConverter(RabbitMessageConverter rabbitMessageConverter) {
        /**
         * 判断是否为null，为null抛出异常
         */
        Preconditions.checkNotNull(rabbitMessageConverter);
        this.rabbitMessageConverter = rabbitMessageConverter;
    }

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        /**
         * 此处可以进行相应的增强
         */
        return rabbitMessageConverter.toMessage(object,messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        /**
         * 此处可以进行相应的增强
         */
        return rabbitMessageConverter.fromMessage(message);
    }
}
