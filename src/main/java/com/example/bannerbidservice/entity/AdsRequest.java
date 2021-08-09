package com.example.bannerbidservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ads_request")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdsRequest extends BaseEntity {
    @Column(name = "simple_create_date", columnDefinition = "INTEGER")
    private Integer simpleDate;

    @Column(name = "category_req", columnDefinition = "VARCHAR(255)")
    private String categoryReq;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "ip_address", columnDefinition = "VARCHAR(255)")
    private String ipAddress;

    public static final class AdsRequestBuilder {
        private AdsRequest adsRequest;

        private AdsRequestBuilder() {
            adsRequest = new AdsRequest();
        }

        public static AdsRequestBuilder anAdsRequest() {
            return new AdsRequestBuilder();
        }

        public AdsRequestBuilder simpleDate(Integer simpleDate) {
            adsRequest.setSimpleDate(simpleDate);
            return this;
        }

        public AdsRequestBuilder categoryReq(String categoryReq) {
            adsRequest.setCategoryReq(categoryReq);
            return this;
        }

        public AdsRequestBuilder userAgent(String userAgent) {
            adsRequest.setUserAgent(userAgent);
            return this;
        }

        public AdsRequestBuilder ipAddress(String ipAddress) {
            adsRequest.setIpAddress(ipAddress);
            return this;
        }

        public AdsRequest build() {
            return adsRequest;
        }
    }
}