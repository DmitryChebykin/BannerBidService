package com.example.bannerbidservice.controller.validator.category.patch;

import com.example.bannerbidservice.controller.dto.PatchCategoryDTO;
import com.example.bannerbidservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.servlet.HandlerMapping;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class CategoryUpdateNamesValidator implements ConstraintValidator<CategoryUniqueNames, PatchCategoryDTO> {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private HttpServletRequest request;

    public void initialize(CategoryUniqueNames constraint) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    public boolean isValid(PatchCategoryDTO value, ConstraintValidatorContext context) {
        Map<String, String> pathVariables = (Map<String, String>) request
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long id = Long.valueOf(pathVariables.get("id"));

        context.disableDefaultConstraintViolation();

        if (value.getCategoryName() == null && value.getReqName() == null) {
            return true;
        }

        boolean present = value.getCategoryName().isPresent();

        if (present && categoryService.isNameCategoryNotUnique(id, value.getCategoryName().get())) {
            context.buildConstraintViolationWithTemplate("Указанное имя категории уже используется")
                    .addPropertyNode("categoryName")
                    .addConstraintViolation();
            return false;
        }

        present = value.getReqName().isPresent();

        if (present && categoryService.isReqNameCategoryNotUnique(id, value.getReqName().get())) {
            context.buildConstraintViolationWithTemplate("Указанное имя запроса категории уже используется")
                    .addPropertyNode("reqName")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}