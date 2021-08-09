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

public class CategoryUpdateIdValidator implements ConstraintValidator<CategoryId, Long> {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private HttpServletRequest request;

    public void initialize(CategoryId constraint) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Map<String, String> pathVariables = (Map<String, String>) request
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long id = Long.valueOf(pathVariables.get("id"));

        context.disableDefaultConstraintViolation();

        if (id == null) {
            context.buildConstraintViolationWithTemplate("Необходимо указать id")
                    .addPropertyNode("id")
                    .addConstraintViolation();
            return false;
        }

        if (!categoryService.existsById(id)) {
            context.buildConstraintViolationWithTemplate("Нет категории с таким id")
                    .addPropertyNode("id")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}