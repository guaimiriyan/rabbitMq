package com.angus.rabbitmq.producer.broker;

import com.angus.rabbitmq.producer.api.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName MessageHolder.java
 * @Description TODO
 * @createTime 2021年02月15日 15:24:00
 */
public class MessageHolder {

    private List<Message> messages = new ArrayList<Message>();


    public static final  ThreadLocal<MessageHolder> threadLocal = new ThreadLocal<MessageHolder>(){
        @Override
        protected MessageHolder initialValue() {
            return new MessageHolder();
        }
    };
    public static void addMessage(Message message){
        threadLocal.get().messages.add(message);
    }

    public static List<Message> clear(){
        List<Message> messages = threadLocal.get().messages;
        threadLocal.remove();
        return messages;
    }
}
