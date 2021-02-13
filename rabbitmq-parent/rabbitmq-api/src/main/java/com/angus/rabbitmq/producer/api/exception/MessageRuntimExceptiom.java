package com.angus.rabbitmq.producer.api.exception;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName MessageRuntimExceptiom.java
 * @Description TODO
 * @createTime 2021年02月13日 01:54:00
 */
public class MessageRuntimExceptiom extends RuntimeException{
    private static final long serialVersionUID = 7735222961886099312L;

    public MessageRuntimExceptiom() {
        super();
    }

    public MessageRuntimExceptiom(String message) {
        super(message);
    }

    public MessageRuntimExceptiom(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageRuntimExceptiom(Throwable cause) {
        super(cause);
    }

    protected MessageRuntimExceptiom(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
