package com.example.bannerbidservice.repository;

import com.example.bannerbidservice.entity.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long>, JpaSpecificationExecutor<Banner> {
    List<Banner> findAllByDeletedFalseAndBannerNameContainingIgnoreCase(String bannerName);



    Banner findByBannerName(String bannerName);

    @Query("select p.id from Banner p where (p.category.id = :id and p.deleted = false )")
    List<Long> findAllIdByDeletedFalseAndCategoryId(Long id);

    List<Banner> findAllByDeletedFalse();

    boolean existsByIdIsNotAndBannerName(Long id, String bannerName);

    boolean existsById(Long id);

    Page<Banner> findAllByDeletedFalse(Pageable pageable);

    Page<Banner> findAllByDeletedFalseAndBannerNameContainingIgnoreCase(String name, Pageable pageable);

    Long countAllByDeletedFalseAndCategoryId(Long id);

//    @Query("select b.id, b.price from Banner b where b.deleted = false and b.category.reqName = :reqName order by b.price desc ")
    List<BannerIdAndPrice> findAllByDeletedFalseAndCategory_ReqName(@Param("reqName") String reqName);
}