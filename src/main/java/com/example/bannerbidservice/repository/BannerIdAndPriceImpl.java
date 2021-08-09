package com.example.bannerbidservice.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BannerIdAndPriceImpl implements BannerIdAndPrice {

    private Long id;
    private BigDecimal price;
}