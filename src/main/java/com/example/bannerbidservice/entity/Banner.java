package com.example.bannerbidservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@DynamicUpdate
@Table(name = "banner")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Banner extends BaseEntity {
    public static final long MAX_PRICE_EXCLUDE = 10000;
    public static final int FRACTIONAL_DIGITS_NUMBER = 2;

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    private String bannerName;

    @Column(nullable = false, precision = 6, scale = 2) // Creates the database field with this size.
    private BigDecimal price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean deleted;

    public static final class BannerBuilder {
        private final Banner banner;

        private BannerBuilder() {
            banner = new Banner();
        }

        public static BannerBuilder aBanner() {
            return new BannerBuilder();
        }

        public BannerBuilder bannerName(String bannerName) {
            banner.setBannerName(bannerName);
            return this;
        }

        public BannerBuilder price(BigDecimal price) {
            banner.setPrice(price);
            return this;
        }

        public BannerBuilder category(Category category) {
            banner.setCategory(category);
            return this;
        }

        public BannerBuilder content(String content) {
            banner.setContent(content);
            return this;
        }

        public BannerBuilder deleted(Boolean deleted) {
            banner.setDeleted(deleted);
            return this;
        }

        public Banner build() {
            return banner;
        }
    }
}