package com.example.bannerbidservice.controller;

import com.example.bannerbidservice.BannerBidServiceApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class Ctrl {

    @GetMapping("/shutdown")
    void shutdown() {
        BannerBidServiceApplication.shutdown();
    }

    @GetMapping("/restart")
    void restart() {
        BannerBidServiceApplication.restart();
    }
}