package com.angus.rabbitmq.producer.broker;

import java.util.concurrent.*;

/**
 * @author angus
 * @version 1.0.0
 * @ClassName AsynSendQueue.java
 * @Description 异步消息发送所需要的线程池
 * @createTime 2021年02月13日 11:25:00
 */
public class AsynBaseQueue {

    //private static final int min_thread = 5;
    private static final int min_thread = Runtime.getRuntime().availableProcessors();
    private static final int max_thread = 10;
    private static final long keep_time = 60L;
    private static final int queue_size = 10;

    private static ExecutorService sendAsyn = new ThreadPoolExecutor(
            min_thread,
            2*min_thread, keep_time,
            TimeUnit.MICROSECONDS,
            new ArrayBlockingQueue<Runnable>(queue_size),
            //创建线程的工厂，此处进行重写
            new ThreadFactory() {
                 @Override
                 public Thread newThread(Runnable r) {
                      Thread thread = newThread(r);
                      thread.setName("angus's rbbitmq asyn thread");
                      return thread;
                 }
            },
            //Method that may be invoked by a ThreadPoolExecutor when execute cannot accept a task.
            new RejectedExecutionHandler() {
                 @Override
                 public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                      System.out.println("reject runnale"+r);
                 }
            }
    );

    public static void  submit(Runnable runnable){
         sendAsyn.submit(runnable);
    }
}
