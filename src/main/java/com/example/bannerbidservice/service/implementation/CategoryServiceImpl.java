package com.example.bannerbidservice.service.implementation;

import com.example.bannerbidservice.controller.dto.AddCategoryDTO;
import com.example.bannerbidservice.controller.dto.CategoryResponseDTO;
import com.example.bannerbidservice.controller.dto.PatchCategoryDTO;
import com.example.bannerbidservice.entity.Category;
import com.example.bannerbidservice.repository.BannerRepository;
import com.example.bannerbidservice.repository.CategoryRepository;
import com.example.bannerbidservice.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryRepository categoryRepository;

    @Resource
    private BannerRepository bannerRepository;

    @Override
    @Transactional
    public boolean existsByName(String name) {
        return categoryRepository.existsByCategoryName(name);
    }

    @Override
    @Transactional
    public List<Category> getCategoriesByNameContainingIgnoreCase(String partName) {
        return categoryRepository.findByCategoryNameContainingIgnoreCase(partName);
    }

    @Override
    @Transactional
    public List<Category> getActiveCategories() {
        return categoryRepository.findAllByDeletedFalse();
    }

    @Override
    @Transactional
    public CategoryResponseDTO getCategoryResponseDTO(AddCategoryDTO addCategoryDTO) {
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();

        Category category = categoryRepository.findByCategoryName(addCategoryDTO.getCategoryName());

        categoryResponseDTO.setCategory(category);

        if (category != null) {
            categoryResponseDTO.setStatus("Unsuccessful");
            categoryResponseDTO.addMessage("Категория с таким именем уже есть");

            return categoryResponseDTO;
        }

        category = addCategoryDTO.toCategory();
        createCategory(category);
        categoryResponseDTO.setCategory(category);

        return categoryResponseDTO;
    }

    @Transactional
    Long createCategory(Category category) {
        return categoryRepository.save(category).getId();
    }

    @Override
    @Transactional
    public boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }

    @Override
    @Transactional
    public boolean isNameCategoryNotUnique(Long id, String name) {
        return categoryRepository.existsByIdIsNotAndCategoryName(id, name);
    }

    @Override
    @Transactional
    public boolean isReqNameCategoryNotUnique(Long id, String name) {
        return categoryRepository.existsByIdIsNotAndReqName(id, name);
    }

    @Override
    @Transactional
    public CategoryResponseDTO getCategoryResponseDTO(PatchCategoryDTO patchCategoryDTO, Long id) {
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();

        categoryResponseDTO.setStatus("Successful");
        categoryResponseDTO.setMessages(new ArrayList<>());
        categoryResponseDTO.setActiveBannersId(new ArrayList<>());

        if (!categoryRepository.existsById(id)) {
            categoryResponseDTO.addMessage("Не найдена категория с таким ID");
            categoryResponseDTO.setStatus("Unsuccessful");
        }

        Category category = categoryRepository.findById(id).get();

        if (patchCategoryDTO.getReqName() != null && patchCategoryDTO.getReqName().isPresent()) {
            String name = patchCategoryDTO.getReqName().get();

            if (!isReqNameCategoryNotUnique(id, name)) {
                category.setReqName(name);
            } else {
                categoryResponseDTO.addMessage("Имя запроса не уникальное");
                categoryResponseDTO.setStatus("Unsuccessful");
            }
        }

        if (patchCategoryDTO.getCategoryName() != null && patchCategoryDTO.getCategoryName().isPresent()) {
            String name = patchCategoryDTO.getCategoryName().get();

            if (!isNameCategoryNotUnique(id, name)) {
                category.setCategoryName(name);
            } else {
                categoryResponseDTO.addMessage("Имя категории не уникальное");
                categoryResponseDTO.setStatus("Unsuccessful");
            }
        }

        if (patchCategoryDTO.getDeleted() != null && patchCategoryDTO.getDeleted().isPresent()) {
            String deleted = patchCategoryDTO.getDeleted().get();

            if (deleted.matches("^true$|^false$")) {
                if (deleted.equals("true")) {
                    Long count = bannerRepository.countAllByDeletedFalseAndCategoryId(id);

                    if (count == null || count == 0) {
                        category.setDeleted(Boolean.valueOf(deleted));
                    } else {
                        categoryResponseDTO.setStatus("Unsuccessful");
                        categoryResponseDTO.addMessage("Есть активные баннеры в данной категории");
                        List<Long> allIdByDeletedFalseAndCategoryId = bannerRepository.findAllIdByDeletedFalseAndCategoryId(id);
                        categoryResponseDTO.setActiveBannersId(allIdByDeletedFalseAndCategoryId);
                    }
                }
            } else {
                categoryResponseDTO.addMessage("Неверное значение пометки удаления");
                categoryResponseDTO.setStatus("Unsuccessful");
            }
        }

        if (categoryResponseDTO.getStatus().equals("Successful")) {
            Category savedCategory = categoryRepository.save(category);
            categoryResponseDTO.setCategory(savedCategory);
        }

        return categoryResponseDTO;
    }
}