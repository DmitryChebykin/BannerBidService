package com.example.bannerbidservice.repository;

import com.example.bannerbidservice.entity.Banner;
import org.springframework.data.rest.core.config.Projection;
import java.math.BigDecimal;
@Projection(
        name = "bannerIdAndPrice",
        types = { Banner.class })
public interface BannerIdAndPrice {
    Long getId();

    BigDecimal getPrice();
}