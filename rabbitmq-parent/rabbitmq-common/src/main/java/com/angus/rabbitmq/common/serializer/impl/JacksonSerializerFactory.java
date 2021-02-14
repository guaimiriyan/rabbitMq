package com.angus.rabbitmq.common.serializer.impl;

import com.angus.rabbitmq.common.serializer.Serializer;
import com.angus.rabbitmq.common.serializer.SerializerFactory;
import com.angus.rabbitmq.producer.api.Message;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName JacksonSerializerFactory.java
 * @Description TODO
 * @createTime 2021年02月14日 15:29:00
 */
public class JacksonSerializerFactory implements SerializerFactory {

    /**
     * 此处为单例数据工程
     */
    public static final SerializerFactory  SerializerFactory = new JacksonSerializerFactory();

    @Override
    public Serializer create(Class clazz) {
        return JacksonSerializer.createParametricType(clazz);
    }
}
