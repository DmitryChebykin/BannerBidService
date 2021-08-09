package com.example.bannerbidservice.repository;

import com.example.bannerbidservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    List<Category> findByCategoryNameContainingIgnoreCase(String partName);

    Category findByReqName(String reqName);

    Category findByCategoryName(String name);

    boolean existsByCategoryName(String name);

    List<Category> findAllByDeletedFalse();

    boolean existsByIdIsNotAndCategoryName(Long id, String name);

    boolean existsByIdIsNotAndReqName(Long id, String name);

    boolean existsByReqName(String reqName);
}