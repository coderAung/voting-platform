package edu.ucsy.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

    @Bean
    TaskScheduler scheduler() {
        var bean = new ThreadPoolTaskScheduler();
        bean.setPoolSize(10);
        bean.setThreadNamePrefix("poll-scheduler-");
        bean.initialize();
        return bean;
    }
}
