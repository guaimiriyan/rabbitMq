package com.angus.rabbitmq.task.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 该类为zookeeperRegisterFactory所使用的配置类
 */
@ConfigurationProperties(prefix = "elastic.job.zk")
@Data
public class JobZookeeperProperties {

	private String namespace;
	
	private String serverLists;
	
	private int maxRetries = 3;

	private int connectionTimeoutMilliseconds = 15000;
	
	private int sessionTimeoutMilliseconds = 60000;
	
	private int baseSleepTimeMilliseconds = 1000;
	
	private int maxSleepTimeMilliseconds = 3000;
	
	private String digest = "";
	
}
