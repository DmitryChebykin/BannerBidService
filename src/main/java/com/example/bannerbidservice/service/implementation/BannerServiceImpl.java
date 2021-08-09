package com.example.bannerbidservice.service.implementation;

import com.example.bannerbidservice.controller.dto.AddBannerDTO;
import com.example.bannerbidservice.controller.dto.BannerResponseDTO;
import com.example.bannerbidservice.controller.dto.PatchBannerDTO;
import com.example.bannerbidservice.entity.Banner;
import com.example.bannerbidservice.entity.Category;
import com.example.bannerbidservice.repository.BannerIdAndPrice;
import com.example.bannerbidservice.repository.BannerRepository;
import com.example.bannerbidservice.repository.CategoryRepository;
import com.example.bannerbidservice.service.BannerService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BannerServiceImpl implements BannerService {
    public static final String UNSUCCESSFUL = "Unsuccessful";
    public static final String SUCCESSFUL = "Successful";
    @Resource
    private final BannerRepository bannerRepository;
    @Resource
    private final CategoryRepository categoryRepository;

    public BannerServiceImpl(BannerRepository bannerRepository, CategoryRepository categoryRepository) {
        this.bannerRepository = bannerRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Banner> getBannersByNameContainingIgnoreCase(String partName) {
        return bannerRepository.findAllByDeletedFalseAndBannerNameContainingIgnoreCase(partName);
    }

    @Override
    @Transactional
    @CacheEvict(value = "active_banners_in_category", allEntries = true)
    public Long createBanner(Banner banner) {
        Category bannerCategory = banner.getCategory();
        Category tableCategory = categoryRepository.findByReqName(bannerCategory.getReqName());

        if (tableCategory != null) {
            banner.setCategory(tableCategory);
        }

        return bannerRepository.save(banner).getId();
    }

    @Override
    @Transactional
    public BannerResponseDTO getAddBannerResponseDTO(AddBannerDTO addBannerDTO) {
        BannerResponseDTO bannerResponseDTO = new BannerResponseDTO();
        Banner banner = bannerRepository.findByBannerName(addBannerDTO.getBannerName());

        if (banner != null) {
            bannerResponseDTO.setStatus(UNSUCCESSFUL);
            bannerResponseDTO.addMessage("Баннер с таким именем уже есть");
            bannerResponseDTO.setBanner(banner);
            return bannerResponseDTO;
        }

        Category category = categoryRepository.findByCategoryName(addBannerDTO.getCategoryName());

        if (category == null) {
            bannerResponseDTO.setStatus(UNSUCCESSFUL);
            bannerResponseDTO.addMessage("Указанной категории не существует");
            return bannerResponseDTO;
        }

        banner = addBannerDTO.toBanner();
        banner.setCategory(category);

        bannerResponseDTO.setBanner(bannerRepository.getById(createBanner(banner)));
        bannerResponseDTO.setStatus(SUCCESSFUL);
        bannerResponseDTO.addMessage("Новый баннер сохранен");

        return bannerResponseDTO;
    }

    @Override
    @Transactional
    public List<Banner> getActiveBanners() {
        return bannerRepository.findAllByDeletedFalse();
    }

    @Override
    @Transactional
    public boolean existsById(Long id) {
        return bannerRepository.existsById(id);
    }

    @Override
    @Transactional
    public boolean isBannerNameNotUnique(Long id, String bannerName) {
        return bannerRepository.existsByIdIsNotAndBannerName(id, bannerName);
    }

    @Override
    @Transactional
    public List<Banner> getAllActiveBannerAsPageable(Integer pageIndex, Integer sizeOfPage) {
        Pageable pageable = PageRequest.of(pageIndex, sizeOfPage);

        Page<Banner> page = bannerRepository.findAllByDeletedFalse(pageable);

        if (page != null) {
            return page.getContent();
        }

        return Collections.emptyList();
    }

    @Override
    @Transactional
    public List<Banner> getBannersByNameContainingIgnoreCaseAsPageable(String nameParam, Integer pageIndex, Integer sizeOfPage) {
        Pageable pageable = PageRequest.of(pageIndex, sizeOfPage);

        Page<Banner> page = bannerRepository.findAllByDeletedFalseAndBannerNameContainingIgnoreCase(nameParam, pageable);

        if (page != null) {
            return page.getContent();
        }

        return Collections.emptyList();
    }

    @Override
    @Transactional
    @CacheEvict(value = "active_banners_in_category", allEntries = true)
    public BannerResponseDTO getAddBannerResponseDTO(PatchBannerDTO patchBannerDTO, Long id) {
        BannerResponseDTO bannerResponseDTO = new BannerResponseDTO();

        bannerResponseDTO.setStatus(SUCCESSFUL);

        idAssignment(id, bannerResponseDTO);

        Banner banner = bannerRepository.findById(id).get();

        nameAssignment(patchBannerDTO, id, bannerResponseDTO, banner);

        categoryAssignment(patchBannerDTO, bannerResponseDTO, banner);

        priceAssignment(patchBannerDTO, bannerResponseDTO, banner);

        contentAssignment(patchBannerDTO, bannerResponseDTO, banner);

        deletedAssignment(patchBannerDTO, bannerResponseDTO, banner);

        if (bannerResponseDTO.getStatus().equals(SUCCESSFUL)) {
            bannerResponseDTO.setBanner(bannerRepository.save(banner));
        }

        return bannerResponseDTO;
    }

    @Override
    @Cacheable(cacheNames = "active_banners_in_category")
    public List<BannerIdAndPrice> getActiveBannersIdByCategory(String category) {
        return bannerRepository.findAllByDeletedFalseAndCategory_ReqName(category);
    }

    public Banner getBanner(Long bannerId) {
        return bannerRepository.getById(bannerId);
    }

    @Override
    public Optional<Banner> getById(Long showingBannerId) {
        return Optional.of(bannerRepository.findAllById(Collections.singletonList(showingBannerId)).stream().iterator().next());
    }

    public boolean existsByReqName(String reqName) {
        return categoryRepository.existsByReqName(reqName);
    }

    @Override
    public void patch(Banner banner) {

    }

    private void idAssignment(Long id, BannerResponseDTO bannerResponseDTO) {
        if (!bannerRepository.existsById(id)) {
            bannerResponseDTO.addMessage("Не найден баннер с таким ID");
            bannerResponseDTO.setStatus("Unsuccessful");
        }
    }

    private void nameAssignment(PatchBannerDTO patchBannerDTO, Long id, BannerResponseDTO bannerResponseDTO, Banner banner) {
        if (patchBannerDTO.getBannerName() != null && patchBannerDTO.getBannerName().isPresent()) {
            String bannerName = patchBannerDTO.getBannerName().get();

            if (!isBannerNameNotUnique(id, bannerName)) {
                banner.setBannerName(bannerName);
            } else {
                bannerResponseDTO.addMessage("Есть другой баннер с таким же именем, нужно другое уникальное имя");
                bannerResponseDTO.setStatus("Unsuccessful");
            }
        }
    }

    private void contentAssignment(PatchBannerDTO patchBannerDTO, BannerResponseDTO bannerResponseDTO, Banner banner) {
        if (patchBannerDTO.getContent() != null && patchBannerDTO.getContent().isPresent()) {
            String content = patchBannerDTO.getContent().get();
            banner.setContent(content);
        }
    }

    private void deletedAssignment(PatchBannerDTO patchBannerDTO, BannerResponseDTO bannerResponseDTO, Banner banner) {
        if (patchBannerDTO.getDeleted() != null && patchBannerDTO.getDeleted().isPresent()) {
            String deleted = patchBannerDTO.getDeleted().get();

            if (deleted.matches("^true$|^false$")) {
                banner.setDeleted(Boolean.valueOf(deleted));
            } else {
                bannerResponseDTO.addMessage("Неверное значение пометки удаления");
                bannerResponseDTO.setStatus("Unsuccessful");
            }
        }
    }

    private void priceAssignment(PatchBannerDTO patchBannerDTO, BannerResponseDTO bannerResponseDTO, Banner banner) {
        if (patchBannerDTO.getPrice() != null && patchBannerDTO.getPrice().isPresent()) {
            BigDecimal bigDecimalPrice = patchBannerDTO.getPrice().get();

            if (bigDecimalPrice.scale() <= Banner.FRACTIONAL_DIGITS_NUMBER && bigDecimalPrice.compareTo(BigDecimal.ZERO) > 0
                    && bigDecimalPrice.compareTo(BigDecimal.valueOf(Banner.MAX_PRICE_EXCLUDE)) < 0) {
                banner.setPrice(bigDecimalPrice);
            } else {
                bannerResponseDTO.addMessage("Неверное значение цены");
                bannerResponseDTO.setStatus("Unsuccessful");
            }
        }
    }

    private void categoryAssignment(PatchBannerDTO patchBannerDTO, BannerResponseDTO bannerResponseDTO, Banner banner) {
        if (patchBannerDTO.getCategoryName() != null && patchBannerDTO.getCategoryName().isPresent()) {
            String categoryName = patchBannerDTO.getCategoryName().get();

            if (categoryRepository.existsByCategoryName(categoryName)) {
                banner.setCategory(categoryRepository.findByCategoryName(categoryName));
            } else {
                bannerResponseDTO.addMessage("Категории нет, сначала создайте категорию");
                bannerResponseDTO.setStatus("Unsuccessful");
            }
        }
    }
}