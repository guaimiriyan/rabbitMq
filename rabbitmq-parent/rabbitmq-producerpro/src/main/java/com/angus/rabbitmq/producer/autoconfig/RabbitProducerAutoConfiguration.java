package com.angus.rabbitmq.producer.autoconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author angus
 * @version 1.0.0
 * @ClassName RabbitProducerAutoConfiguration.java
 * @Description 添加自动封装类
 * @createTime 2021年02月13日 13:46:00
 */
@Component
@ComponentScan(value = "com.angus.rabbitmq.producer.*")
public class RabbitProducerAutoConfiguration {
    /**
     * 自动扫描该路径下的并交给spring管理
     */
}
