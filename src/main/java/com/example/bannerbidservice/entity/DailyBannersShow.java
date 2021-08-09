package com.example.bannerbidservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;

@Entity
@DynamicUpdate
@Table(name = "daily_show")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailyBannersShow extends BaseEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adsrequest_id", nullable = false)
    private AdsRequest adsRequest;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "banner_id", nullable = false)
    private Banner banner;
}