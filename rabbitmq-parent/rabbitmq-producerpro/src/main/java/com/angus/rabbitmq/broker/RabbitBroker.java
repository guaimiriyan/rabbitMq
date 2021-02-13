package com.angus.rabbitmq.broker;

import com.angus.rabbitmq.api.Message;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName RabbitBroker.java
 * @Description 创建三种方式分别对应的投递方式
 * @createTime 2021年02月13日 10:34:00
 */
public interface RabbitBroker {

    void sendQuick(Message message);
    void sendConfirm(Message message);
    void sendRapid(Message message);
}