package com.angus.rabbitmq.producer.api;

import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName MessageProducer.java
 * @Description TODO
 * @createTime 2021年02月13日 01:59:00
 */
public interface MessageProducer {

    /**
     * 使用一般的发送接口
     * @param message
     */
    void send(Message message);

    /**
     * 产生回调的发送接口
     * @param message
     * @param callBack
     */
    void send(Message message,SendCallBack callBack);

    /**
     * 批量message的发送接口
     * @param messageList
     */
    void send(List<Message> messageList);
}
