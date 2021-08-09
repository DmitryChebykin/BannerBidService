package com.example.bannerbidservice.util;

import com.example.bannerbidservice.entity.Banner;
import com.example.bannerbidservice.entity.Category;
import com.example.bannerbidservice.service.BannerService;
import com.example.bannerbidservice.service.CategoryService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class UtilApp {
    @Resource
    private BannerService bannerService;
    @Resource
    private ApplicationArguments applicationArguments;
    @Resource
    private CategoryService categoryService;

    @EventListener
    public void run(ApplicationReadyEvent event) {
        if (applicationArguments.containsOption("init")) {
            InitDB();
        }
    }

    private void InitDB() {
        String[] categories = {"Alco", "Sport", "Food"};
        BigDecimal[] prices = {BigDecimal.valueOf(1000), BigDecimal.valueOf(2000), BigDecimal.valueOf(5000)};
        Category category = null;

        for (int i = 1; i < 300; i++) {
            int categoriesRandomIndex = ThreadLocalRandom.current().nextInt(0, 3);
            category = Category.CategoryBuilder.aCategory().name(categories[categoriesRandomIndex]).reqName(categories[categoriesRandomIndex]+"_req").deleted(false).build();

            int pricesRandomIndex = ThreadLocalRandom.current().nextInt(0, 3);

            Banner banner = Banner.BannerBuilder.aBanner().bannerName(categories[categoriesRandomIndex] + i)
                    .category(category)
                    .price(prices[pricesRandomIndex])
                    .content("Текст баннера " + category.getReqName() + " " + i)
                    .deleted(ThreadLocalRandom.current().nextBoolean())
                    .build();
            bannerService.createBanner(banner);
        }
    }
}