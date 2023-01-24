package io.task.scheduler.config;

import io.task.scheduler.SchedulerService;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@ConfigurationProperties(prefix = "scheduler")
@EnableScheduling
@Setter
public class SchedulerConfiguration implements SchedulingConfigurer {

    private String cycleInterval;

    @Bean
    public Executor taskExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        long cycle = getCycleValue();
        taskRegistrar.setScheduler(taskExecutor());
        taskRegistrar.addTriggerTask(new SchedulerService(), triggerContext -> {
            Optional<Instant> previous = Optional.ofNullable(triggerContext.lastCompletion());
            if (previous.isEmpty()) {
                return Instant.now();
            }
            return previous.get().plusMillis(cycle);
        });
    }

    private long getCycleValue() {
        long cycle;
        try {
            cycle = Long.parseLong(cycleInterval);
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal cycle-interval value");
            System.out.println("Please enter a number value for cycle-interval in milliseconds");
            System.out.println("The value is set back to default for now");
            cycle = 60000L;
        }
        return cycle;
    }
}
