package com.zeaho.TCP.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@EnableScheduling  //定时任务
@RestController
public class RedisCount {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Async
    @Scheduled(cron = "59 59 23 * * ? ")
    public void delCount() {
        System.out.println("定时删除任务执行");
        ValueOperations<String, String> svro = stringRedisTemplate.opsForValue();
        String strCount = svro.get("count");
        if (strCount != null) {
            stringRedisTemplate.delete("count");
        }
    }
}

