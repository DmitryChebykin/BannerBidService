package com.example.bannerbidservice.service.implementation;

import com.example.bannerbidservice.entity.AdsRequest;
import com.example.bannerbidservice.entity.Banner;
import com.example.bannerbidservice.entity.DailyBannersShow;
import com.example.bannerbidservice.repository.DailyBannersShowRepository;
import com.example.bannerbidservice.service.DailyBannersShowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Set;

@Service
public class DailyBannersShowServiceImpl implements DailyBannersShowService {
    @Resource
    DailyBannersShowRepository dailyBannersShowRepository;

    @Override
    @Transactional
    public Long save(Banner banner, AdsRequest adsRequest) {
        return dailyBannersShowRepository.save(new DailyBannersShow(adsRequest, banner)).getId();
    }
    @Transactional
    @Override
    public void clearAllNotes() {
        dailyBannersShowRepository.truncateTable();
    }

    @Override
    public Set<Long> findAllByCategoryReqAndIpAddress(String categoryReq, String ipAddress, String userAgent) {
        return dailyBannersShowRepository.findMyId(categoryReq, ipAddress, userAgent);
    }
}