package com.example.bannerbidservice.service.implementation;

import com.example.bannerbidservice.entity.AdsRequest;
import com.example.bannerbidservice.entity.Banner;
import com.example.bannerbidservice.repository.AdsRequestRepository;
import com.example.bannerbidservice.repository.BannerIdAndPrice;
import com.example.bannerbidservice.service.AdsRequestService;
import com.example.bannerbidservice.service.BannerService;
import com.example.bannerbidservice.service.DailyBannersShowService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdsRequestServiceImpl implements AdsRequestService {
    public static final String DATE_AS_INTEGER = "yyyyMMdd";

    private final Random rand = SecureRandom.getInstanceStrong();

    @Resource
    private AdsRequestRepository adsRequestRepository;

    @Resource
    private DailyBannersShowService dailyBannersShowService;

    @Resource
    private BannerService bannerService;

    public AdsRequestServiceImpl() throws NoSuchAlgorithmException {
    }

    @Override
    @Transactional
    public Long addRequest(String category, String ipAddress, String userAgent) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_AS_INTEGER);

        Integer dateAsInteger = Integer.parseInt(simpleDateFormat.format(new Date()));

        AdsRequest adsRequest = AdsRequest.AdsRequestBuilder.anAdsRequest()
                .categoryReq(category)
                .ipAddress(ipAddress)
                .simpleDate(dateAsInteger)
                .userAgent(userAgent)
                .build();

        return adsRequestRepository.save(adsRequest).getId();
    }

    @Override
    @Cacheable(cacheNames = "showed_banners")
    public Set<Long> getShowedBannersIdByClientAndCategory(String categoryReq, String ipAddress, String userAgent) {
        return dailyBannersShowService.findAllByCategoryReqAndIpAddress(categoryReq, ipAddress, userAgent);
    }

    @Override
    public Optional<String> getBannerContent(String categoryReq, String ipAddress, String userAgent) {
        Long showingTextBannerId = getShowingBannerId(categoryReq, ipAddress, userAgent);
        String content = null;

        if (showingTextBannerId > 0) {
            Optional<Banner> banner = bannerService.getById(showingTextBannerId);

            if (banner.isPresent()) {
                content = banner.map(Banner::getContent).get();
            }
        }

        return Optional.ofNullable(content);
    }

    private List<Long> getFilteredMaxPriceBannersIdListByCategory(String categoryReq, Set<Long> ignoreIds) {
        List<BannerIdAndPrice> activeBannersIdByCategory = bannerService.getActiveBannersIdByCategory(categoryReq);

        BigDecimal maxPrice = activeBannersIdByCategory.stream().max(Comparator.comparing(BannerIdAndPrice::getPrice)).get().getPrice();
        List<Long> maxPriceCategoryBannersIdList = activeBannersIdByCategory.stream().filter(bannerIDAndPrice -> bannerIDAndPrice.getPrice().compareTo(maxPrice) == 0)
                .map(BannerIdAndPrice::getId).collect(Collectors.toList());

        return maxPriceCategoryBannersIdList.stream().filter(e -> !ignoreIds.contains(e)).collect(Collectors.toList());
    }

    private Long getRandomListElement(List<Long> id) {
        return id.get(rand.nextInt(id.size()));
    }

    @Transactional
    public Long saveOneBannerShow(Banner banner, AdsRequest adsRequest) {
        return dailyBannersShowService.save(banner, adsRequest);
    }

    private Long getShowingBannerId(String categoryReq, String ipAddress, String userAgent) {
        Long currentAdsRequestId = addRequest(categoryReq, ipAddress, userAgent);
        Long bannerId = -1L;

        if (!bannerService.existsByReqName(categoryReq)) {
            return bannerId;
        }

        Set<Long> allReadyShowedBannersId = getShowedBannersIdByClientAndCategory(categoryReq, ipAddress, userAgent);
        List<Long> notShowedBannersId = getFilteredMaxPriceBannersIdListByCategory(categoryReq, allReadyShowedBannersId);

        if (notShowedBannersId != null && !notShowedBannersId.isEmpty()) {
            bannerId = getRandomListElement(notShowedBannersId);
            saveOneBannerShow(bannerService.getBanner(bannerId), adsRequestRepository.getById(currentAdsRequestId));
        }

        return bannerId;
    }
}