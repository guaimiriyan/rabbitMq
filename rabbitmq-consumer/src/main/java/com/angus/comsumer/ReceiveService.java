package com.angus.comsumer;

import com.angus.properties.angusMqProperties;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;

import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@EnableConfigurationProperties(angusMqProperties.class)
public class ReceiveService {


    /**
     * 	组合使用监听
     * 	@RabbitListener @QueueBinding @Queue @Exchange
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue-1", durable = "true"),
            exchange = @Exchange(name = "exchange-angus",
                    durable = "true",
                    type = "topic",
                    ignoreDeclarationExceptions = "true"),
            key = "angus.*"
    )
    )
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws IOException {
        //1、该处为处理业务代码
        System.out.println(message);
        //2、由于该处是手动，需要从channl里获取值积极性ask
        Long aLong = message.getHeaders().get(AmqpHeaders.DELIVERY_TAG, Long.class);
//        true to acknowledge all messages up to and including the supplied delivery tag;
//        false to acknowledge just the supplied delivery tag.
        channel.basicAck(aLong,false);

    }


}
