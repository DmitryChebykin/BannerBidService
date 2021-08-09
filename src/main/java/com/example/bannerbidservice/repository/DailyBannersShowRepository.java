package com.example.bannerbidservice.repository;

import com.example.bannerbidservice.entity.DailyBannersShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface DailyBannersShowRepository extends JpaRepository<DailyBannersShow, Long>, JpaSpecificationExecutor<DailyBannersShow> {
    @Query("select distinct d.banner.id from DailyBannersShow d where d.banner.category.reqName = :reqName and d.adsRequest.ipAddress = :ipAddress and d.adsRequest.userAgent = :userAgent")
    Set<Long> findMyId(@Param("reqName") String reqName, @Param("ipAddress") String ipAddress, @Param("userAgent") String userAgent);

    @Modifying
    @Query(
            value = "truncate table daily_show",
            nativeQuery = true
    )
    void truncateTable();
}