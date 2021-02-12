package com.angus.myTest;

import com.angus.producer.sendService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class sendTest {
    @Autowired
    private sendService sendService;
    @Test
    public void  sendTest() throws InterruptedException {
        Map<String,Object> properties = new HashMap<>();
        properties.put("angus","lee");
        sendService.send("angus first use rabbitmq!",properties);
        //休眠查看结果
        Thread.sleep(10000);
    }
}
