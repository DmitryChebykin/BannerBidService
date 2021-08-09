package com.example.bannerbidservice.controller.validator.banner;

import com.example.bannerbidservice.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.servlet.HandlerMapping;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class BannerUpdateIdValidator implements ConstraintValidator<BannerId, Long> {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private BannerService bannerService;

    @Override
    public void initialize(final BannerId constraintAnnotation) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
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

        if (!bannerService.existsById(id)) {
            context.buildConstraintViolationWithTemplate("Нет баннера с таким id")
                    .addPropertyNode("id")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}