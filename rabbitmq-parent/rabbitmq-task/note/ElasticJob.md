### ElasticJob分布式任务框架
#### 一、官方分档
* [esjob官方文档]()

#### 二、代码编写流程
1. 在pom.xml中进行依赖的添加
```
<!--  elastic-job dependency -->
	    <dependency>
	      <groupId>com.dangdang</groupId>
	      <artifactId>elastic-job-lite-core</artifactId>
	      <version>${elastic-job.version}</version>
	    </dependency>
	    <dependency>
	      <groupId>com.dangdang</groupId>
	      <artifactId>elastic-job-lite-spring</artifactId>
	      <version>${elastic-job.version}</version>
	    </dependency>
```
2. 添加基本的配置文件在application.xml
```properties
server.port=8881


elastic.job.zk.namespace=elastic-job
elastic.job.zk.serverLists=192.168.11.111:2181,192.168.11.112:2181,192.168.11.113:2181
#zookeeper.address=192.168.11.111:2181,192.168.11.112:2181,192.168.11.113:2181
#zookeeper.namespace=elastic-job
#zookeeper.connectionTimeout=10000
#zookeeper.sessionTimeout=10000
#zookeeper.maxRetries=3


#simpleJob.cron=0/5 * * * * ?
##simpleJob.cron=00 03 21 * * ?
#simpleJob.shardingTotalCount=5
#simpleJob.shardingItemParameters=0=beijing,1=shanghai,2=changchun,3=changsha,4=hangzhou
#simpleJob.jobParameter=source1=public,source2=private
#simpleJob.failover=true
#simpleJob.monitorExecution=true
#simpleJob.monitorPort=8889
#simpleJob.maxTimeDiffSeconds=-1
#simpleJob.jobShardingStrategyClass=com.dangdang.ddframe.job.lite.api.strategy.impl.AverageAllocationJobShardingStrategy

#dataflowJob.cron=0/10 * * * * ?
#dataflowJob.shardingTotalCount=2
#dataflowJob.shardingItemParameters=0=Beijing,1=Shanghai

spring.datasource.url=jdbc:mysql://localhost:3306/elasticjob?useUnicode=true&characterEncoding=utf-8&verifyServerCertificate=false&useSSL=false&requireSSL=false
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root 



```

3. 进行基本配置类的编写
* 根据配置进行zookeeperConfig的配置
* 配置JobCoreConfiguration
* 配置LiteJobConfiguration
* 配置SpringJobScheduler
