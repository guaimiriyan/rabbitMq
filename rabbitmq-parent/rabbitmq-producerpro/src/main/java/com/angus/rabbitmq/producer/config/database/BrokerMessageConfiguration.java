package com.angus.rabbitmq.producer.config.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * @author angus
 * @version 1.0.0
 * @ClassName BrokerMessageConfiguration.java
 * @Description TODO
 * @createTime 2021年02月15日 13:59:00
 */
@Configuration
public class BrokerMessageConfiguration {

    @Autowired
    private DataSource rabbitProducerDataSource;

    @Value("classpath:rabbit-producer-message-schema.sql")
    private Resource sqlScript;


    /**
     * @title dataSourceInitializer，这个配置主要是为了在启动之前做一些初始化炒作
     * @description 创建数据源
     * @author admin []
     * @updateTime 2021/2/15 [] org.springframework.jdbc.datasource.init.DataSourceInitializer
     * @throws
     */
    @Bean
    public DataSourceInitializer dataSourceInitializer(){
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(rabbitProducerDataSource);
        dataSourceInitializer.setDatabasePopulator(populator());
        return dataSourceInitializer;
    }


    private ResourceDatabasePopulator populator(){
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(sqlScript);
        return resourceDatabasePopulator;
    }

}
