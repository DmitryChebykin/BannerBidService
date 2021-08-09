package com.example.bannerbidservice.service;

import com.example.bannerbidservice.entity.AdsRequest;
import com.example.bannerbidservice.entity.Banner;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

public interface DailyBannersShowService {

    Long save(Banner banner, AdsRequest adsRequest);

    void clearAllNotes();

    Set<Long> findAllByCategoryReqAndIpAddress(String categoryReq, String ipAddress, String userAgent);
}