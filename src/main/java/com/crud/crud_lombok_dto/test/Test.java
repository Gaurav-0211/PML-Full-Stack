package com.crud.crud_lombok_dto.test;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Test {
    @Scheduled(cron = "0 * * * * *")
    public static void executeTaskEveryMinute() {
        System.out.println("Task executed at: " + System.currentTimeMillis());
    }

    // This method will run every day at 8 AM
    @Scheduled(cron = "0 0 8 * * *")
    public void executeTaskDailyAt8AM() {
        System.out.println("Daily task executed at 8 AM.");
    }
}
