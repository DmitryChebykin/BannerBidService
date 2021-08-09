package com.example.bannerbidservice.util;

import com.example.bannerbidservice.service.DailyBannersShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledService {
    @Autowired
    DailyBannersShowService dailyBannersShowService;

    @Scheduled(cron = "@midnight")
//    @Scheduled(cron = "30 * * * * *")
    public void clearAllNotes() {
        dailyBannersShowService.clearAllNotes();
    }
}