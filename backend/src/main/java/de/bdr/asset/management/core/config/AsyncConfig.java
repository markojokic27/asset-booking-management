package de.bdr.asset.management.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.reflect.Method;

/**
 * Configuration for handling Async processes
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {

        return (Throwable ex, Method method, Object... params) -> {

            log.error("Async exception caught in method: {}", method.getName());
            log.error("Class: {}", method.getDeclaringClass().getSimpleName());
            log.error("Reason why: {}", ex.getMessage());

            log.error("Parameters in method:");
            for (Object param : params) {

                log.error(" -> {}", param);
            }
        };
    }
}
