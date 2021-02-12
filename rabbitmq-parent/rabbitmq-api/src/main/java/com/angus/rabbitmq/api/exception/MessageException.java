package com.angus.rabbitmq.api.exception;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName MessageException.java
 * @Description TODO
 * @createTime 2021年02月13日 01:54:00
 */
public class MessageException extends Exception{
    private static final long serialVersionUID = -1502282120244279258L;

    public MessageException() {
        super();
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }

    protected MessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
