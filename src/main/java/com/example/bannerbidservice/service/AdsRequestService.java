package com.example.bannerbidservice.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.Set;

public interface AdsRequestService {
    @Transactional
    Long addRequest(String category, String ipAddress, String userAgent);


    Set<Long> getShowedBannersIdByClientAndCategory(String categoryReq, String ipAddress, String userAgent);

    Optional<String> getBannerContent(String categoryReq, String ipAddress, String userAgent);
}