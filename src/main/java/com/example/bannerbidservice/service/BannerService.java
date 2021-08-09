package com.example.bannerbidservice.service;

import com.example.bannerbidservice.controller.dto.AddBannerDTO;
import com.example.bannerbidservice.controller.dto.BannerResponseDTO;
import com.example.bannerbidservice.controller.dto.PatchBannerDTO;
import com.example.bannerbidservice.entity.Banner;
import com.example.bannerbidservice.repository.BannerIdAndPrice;
import java.util.List;
import java.util.Optional;

public interface BannerService {
    List<Banner> getBannersByNameContainingIgnoreCase(String partName);

    Long createBanner(Banner banner);

    BannerResponseDTO getAddBannerResponseDTO(AddBannerDTO addBannerDTO);

    List<Banner> getActiveBanners();

    boolean existsById(Long id);

    boolean isBannerNameNotUnique(Long id, String bannerName);

    List<Banner> getAllActiveBannerAsPageable(Integer pageIndex, Integer sizeOfPage);

    List<Banner> getBannersByNameContainingIgnoreCaseAsPageable(String nameParam, Integer pageIndex, Integer sizeOfPage);

    BannerResponseDTO getAddBannerResponseDTO(PatchBannerDTO patchBannerDTO, Long id);

    List<BannerIdAndPrice> getActiveBannersIdByCategory(String category);

    Banner getBanner(Long bannerId);

    Optional<Banner> getById(Long showingBannerId);

    boolean existsByReqName(String name);

    void patch(Banner banner);
}