package com.agrosellnova.Agrosellnova.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);      // Hilos mínimos
        executor.setMaxPoolSize(20);      // Hilos máximos
        executor.setQueueCapacity(100);   // Cola de espera
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}
