package com.angus.rabbitmq.common.serializer;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName SerializerFactory.java
 * @Description TODO
 * @createTime 2021年02月14日 15:21:00
 */
public interface SerializerFactory {

    Serializer create(Class clazz);
}
