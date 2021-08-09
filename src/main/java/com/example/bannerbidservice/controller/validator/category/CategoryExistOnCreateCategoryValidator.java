package com.example.bannerbidservice.controller.validator.category;

import com.example.bannerbidservice.controller.dto.AddCategoryDTO;
import com.example.bannerbidservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CategoryExistOnCreateCategoryValidator implements ConstraintValidator<CategoryExistOnCreateCategory, AddCategoryDTO> {
    @Autowired
    private CategoryService categoryService;

    @Override
    public void initialize(CategoryExistOnCreateCategory constraintAnnotation) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public boolean isValid(AddCategoryDTO value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if (categoryService.existsByName(value.getCategoryName())) {
            context.buildConstraintViolationWithTemplate("Категория с таким именем уже есть БД")
                    .addPropertyNode("categoryName")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}