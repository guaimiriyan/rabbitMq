package com.angus.rabbitmq.producer.api;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName MessageType.java
 * @Description TODO
 * @createTime 2021年02月13日 01:36:00
 */
public class MessageType {
    //该消息使用快速消息，不使用confirm
    public static final String QUICK = "0";

    //不保证消息百分之百投递，但是需要confirm
    public static final String CONFIRM = "1";

    //保证消息百分之百投递，且需要confirm
    public static final String RELIANT  = "2";
}
