package com.angus.rabbitmq.common.serializer;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName serializer.java
 * @Description 该类为基本的序列化与反序列化接口
 * @createTime 2021年02月14日 15:14:00
 */
public interface Serializer {
    /**
     * 将对象序列化为byte[]数组
     * @param obj
     * @return
     */
    byte[] serializer(Object obj);

    /**
     * 将对象序列化为String
     * @param obj
     * @return
     */
    String serializerToStr(Object obj);

    /**
     * 通过byte[]将其反序列化为对象
     * @param obj
     * @param <T>
     * @return
     */
    <T> T deserializer(byte[] obj);

    /**
     * 将字符串反序列化为对象
     * @param objStr
     * @param <T>
     * @return
     */
    <T> T deserializerByStr(String objStr);
}
