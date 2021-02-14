package com.angus.rabbitmq.task.parser;

import com.angus.rabbitmq.task.annotation.ElasticJobConfig;
import com.angus.rabbitmq.task.config.JobZookeeperProperties;
import com.angus.rabbitmq.task.enums.ElasticJobTypeEnum;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.executor.handler.JobProperties;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author angus
 * @version 1.0.0
 * @ClassName ElasticJobConfParser.java
 * @Description 进行@ElasticJobConfig注解的解释与封装
 * @createTime 2021年02月14日 18:38:00
 */
public class ElasticJobConfParser implements ApplicationListener<ApplicationReadyEvent> {


    private JobZookeeperProperties jobZookeeperProperties;

    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    public ElasticJobConfParser(JobZookeeperProperties jobZookeeperProperties, ZookeeperRegistryCenter zookeeperRegistryCenter) {
        this.jobZookeeperProperties = jobZookeeperProperties;
        this.zookeeperRegistryCenter = zookeeperRegistryCenter;
    }

    /**
     * ApplicationReadyEvent整个环境已经准备好了之后进行数据的封装和对象的创建
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            //1、获取上下文
            ConfigurableApplicationContext applicationContext = event.getApplicationContext();
            //2、从上下文里获取使用ElasticJobConfig的bean
            Map<String, Object> jobConfigs = applicationContext.getBeansWithAnnotation(ElasticJobConfig.class);
            //3、循环将他们进行注入
            for (Object value : jobConfigs.values()) {
                Class<?> clazz = value.getClass();
                String className = clazz.getName();
                //由于某些类中有内部类会导致class下带有$,此处需要将其除去
                if (className.indexOf("$")>0){
                    clazz = Class.forName(className.substring(0,className.indexOf("$")));
                }

                //获取该接口类型
                String jobTypeName = "";
                Class<?>[] interfaces = clazz.getInterfaces();
                for (Class<?> anInterface : interfaces) {
                    Class<?>[] jobInterfaces = anInterface.getInterfaces();
                    for (Class<?> jobInterface : jobInterfaces) {
                        if (jobInterface.getSimpleName().equals("ElasticJob")){
                            jobTypeName = anInterface.getSimpleName();
                        }
                    }

                }
                //4、需要从注解里获取出真实的配置
                ElasticJobConfig conf = clazz.getAnnotation(ElasticJobConfig.class);
                String jobClass = clazz.getName();
                String jobName = this.jobZookeeperProperties.getNamespace() + "." + conf.name();
                String cron = conf.cron();
                String shardingItemParameters = conf.shardingItemParameters();
                String description = conf.description();
                String jobParameter = conf.jobParameter();
                String jobExceptionHandler = conf.jobExceptionHandler();
                String executorServiceHandler = conf.executorServiceHandler();

                String jobShardingStrategyClass = conf.jobShardingStrategyClass();
                String eventTraceRdbDataSource = conf.eventTraceRdbDataSource();
                String scriptCommandLine = conf.scriptCommandLine();

                boolean failover = conf.failover();
                boolean misfire = conf.misfire();
                boolean overwrite = conf.overwrite();
                boolean disabled = conf.disabled();
                boolean monitorExecution = conf.monitorExecution();
                boolean streamingProcess = conf.streamingProcess();

                int shardingTotalCount = conf.shardingTotalCount();
                int monitorPort = conf.monitorPort();
                int maxTimeDiffSeconds = conf.maxTimeDiffSeconds();
                int reconcileIntervalMinutes = conf.reconcileIntervalMinutes();
                //4、根据基本步骤是需要创建JobCore
                JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder(jobName, cron, shardingTotalCount)
                        .misfire(misfire)
                        .jobParameter(jobParameter)
                        .shardingItemParameters(shardingItemParameters)
                        .failover(failover)
                        .description(description)
                        .jobProperties(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(), jobExceptionHandler)
                        .jobProperties(JobProperties.JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER.getKey(), executorServiceHandler)
                        .build();
                //5、获取是哪一种类型的定时任务
                JobTypeConfiguration jobTypeConfiguration = null;
                if (ElasticJobTypeEnum.SIMPLE.getType().equals(jobTypeName)){
                    jobTypeConfiguration = new SimpleJobConfiguration(jobCoreConfiguration,jobClass);
                }
                if (ElasticJobTypeEnum.DATAFLOW.getType().equals(jobTypeName)){
                    jobTypeConfiguration = new DataflowJobConfiguration(jobCoreConfiguration,jobClass,streamingProcess);
                }
                if (ElasticJobTypeEnum.SCRIPT.getType().equals(jobTypeName)){
                    jobTypeConfiguration = new ScriptJobConfiguration(jobCoreConfiguration,scriptCommandLine);
                }
                //5.配置LiteJobConfiguration
                LiteJobConfiguration jobConfig = LiteJobConfiguration
                        .newBuilder(jobTypeConfiguration)
                        .overwrite(overwrite)
                        .disabled(disabled)
                        .monitorPort(monitorPort)
                        .monitorExecution(monitorExecution)
                        .maxTimeDiffSeconds(maxTimeDiffSeconds)
                        .jobShardingStrategyClass(jobShardingStrategyClass)
                        .reconcileIntervalMinutes(reconcileIntervalMinutes)
                        .build();

                //6.最后将其交给Spring管理，通过spring BeanDefinition
                BeanDefinitionBuilder beanDefinitionBuilder =  BeanDefinitionBuilder.rootBeanDefinition(SpringJobScheduler.class);
                beanDefinitionBuilder.setInitMethodName("init");
                beanDefinitionBuilder.setScope("prototype");
                //添加的任务类
                //添加bean构造参数，相当于添加自己的真实的任务实现类
                if (!ElasticJobTypeEnum.SCRIPT.getType().equals(jobTypeName)) {
                    beanDefinitionBuilder.addConstructorArgValue(value);
                }
                //添加zookeeper注册中心
                beanDefinitionBuilder.addConstructorArgValue(zookeeperRegistryCenter);
                //添加LiteJobConfiguration
                beanDefinitionBuilder.addConstructorArgValue(jobConfig);

                //	如果有eventTraceRdbDataSource 则也进行添加
                if (StringUtils.hasText(eventTraceRdbDataSource)) {
                    BeanDefinitionBuilder rdbFactory = BeanDefinitionBuilder.rootBeanDefinition(JobEventRdbConfiguration.class);
                    rdbFactory.addConstructorArgReference(eventTraceRdbDataSource);
                    beanDefinitionBuilder.addConstructorArgValue(rdbFactory.getBeanDefinition());
                }

                //  添加监听
                List<?> elasticJobListeners = getTargetElasticJobListeners(conf);
                beanDefinitionBuilder.addConstructorArgValue(elasticJobListeners);

                // 将其交给spring管理
                DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
                String registerBeanName = conf.name() + "SpringJobScheduler";
                beanFactory.registerBeanDefinition(registerBeanName, beanDefinitionBuilder.getBeanDefinition());
                SpringJobScheduler scheduler = (SpringJobScheduler)applicationContext.getBean(registerBeanName);
                scheduler.init();


            }
        }catch (Exception e){
            System.out.println("elasticjob 启动异常, 系统强制退出"+ e);
            System.exit(1);

        }
    }

    private List<BeanDefinition> getTargetElasticJobListeners(ElasticJobConfig conf) {
        List<BeanDefinition> result = new ManagedList<BeanDefinition>(2);
        String listeners = conf.listener();
        if (StringUtils.hasText(listeners)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(listeners);
            factory.setScope("prototype");
            result.add(factory.getBeanDefinition());
        }

        String distributedListeners = conf.distributedListener();
        long startedTimeoutMilliseconds = conf.startedTimeoutMilliseconds();
        long completedTimeoutMilliseconds = conf.completedTimeoutMilliseconds();

        if (StringUtils.hasText(distributedListeners)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(distributedListeners);
            factory.setScope("prototype");
            factory.addConstructorArgValue(Long.valueOf(startedTimeoutMilliseconds));
            factory.addConstructorArgValue(Long.valueOf(completedTimeoutMilliseconds));
            result.add(factory.getBeanDefinition());
        }
        return result;
    }
}
