package com.example.bannerbidservice.controller.validator.banner;

import com.example.bannerbidservice.controller.dto.PatchBannerDTO;
import com.example.bannerbidservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CategoryExistValidator implements ConstraintValidator<CategoryExist, PatchBannerDTO> {
    @Autowired
    private CategoryService categoryService;

    @Override
    public void initialize(CategoryExist constraintAnnotation) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public boolean isValid(PatchBannerDTO value, ConstraintValidatorContext context) {
        if (value.getCategoryName() == null) {
            return true;
        }

        context.disableDefaultConstraintViolation();

        if (value.getCategoryName().isPresent() && !categoryService.existsByName(value.getCategoryName().get())) {
            context.buildConstraintViolationWithTemplate("Категории с таким именем нет в БД")
                    .addPropertyNode("category")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}