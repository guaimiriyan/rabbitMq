package com.angus.rabbitmq.producer.task;

import com.angus.rabbitmq.producer.api.Message;
import com.angus.rabbitmq.producer.broker.RabbitBroker;
import com.angus.rabbitmq.producer.constant.BrokerMessageStatus;
import com.angus.rabbitmq.producer.entity.BrokerMessage;
import com.angus.rabbitmq.producer.service.MessageStoreService;
import com.angus.rabbitmq.task.annotation.ElasticJobConfig;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName reliantDataFlowJob.java
 * @Description TODO
 * @createTime 2021年02月15日 15:11:00
 */
@Component
@ElasticJobConfig(
        name= "com.bfxy.rabbit.producer.task.RetryMessageDataflowJob",
        cron= "0/10 * * * * ?",
        description = "可靠性投递消息补偿任务",
        overwrite = true,
        shardingTotalCount = 1
)
public class reliantDataFlowJob implements DataflowJob<BrokerMessage> {
    @Autowired
    private MessageStoreService messageStoreService;

    @Autowired
    private RabbitBroker rabbitBroker;

    @Override
    public List<BrokerMessage> fetchData(ShardingContext shardingContext) {
        //从此处抓取
        List<BrokerMessage> brokerMessages = messageStoreService.fetchTimeOutMessage4Retry(BrokerMessageStatus.SEND_FAIL);
        return brokerMessages;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<BrokerMessage> data) {
        //循环处理消息
        data.forEach(brokerMessage -> {
            String messageId = brokerMessage.getMessageId();
            if (brokerMessage.getTryCount()<= 3){
                //	每次重发的时候要更新一下try count字段
                this.messageStoreService.updateTryCount(messageId);
                rabbitBroker.sendRapid(brokerMessage.getMessage());
            }else {
                this.messageStoreService.failure(messageId);
            }
        });

    }
}
