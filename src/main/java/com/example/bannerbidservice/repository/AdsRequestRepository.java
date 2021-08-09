package com.example.bannerbidservice.repository;

import com.example.bannerbidservice.entity.AdsRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface AdsRequestRepository extends JpaRepository<AdsRequest, Long>, JpaSpecificationExecutor<AdsRequest> {

    List<Long> findAllByCreatedOnAndIpAddressAndUserAgent(Date date, String ip, String agent);
}