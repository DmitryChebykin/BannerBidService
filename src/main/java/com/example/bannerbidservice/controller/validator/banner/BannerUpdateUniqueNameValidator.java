package com.example.bannerbidservice.controller.validator.banner;

import com.example.bannerbidservice.controller.dto.PatchBannerDTO;
import com.example.bannerbidservice.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.servlet.HandlerMapping;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class BannerUpdateUniqueNameValidator implements ConstraintValidator<BannerUniqueName, PatchBannerDTO> {
    @Autowired
    private BannerService bannerService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public void initialize(BannerUniqueName constraintAnnotation) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public boolean isValid(PatchBannerDTO value, ConstraintValidatorContext context) {
        Map<String, String> pathVariables = (Map<String, String>) request
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long id = Long.valueOf(pathVariables.get("id"));

        context.disableDefaultConstraintViolation();

        if (value.getBannerName() == null) {
            return true;
        }

        boolean present = value.getBannerName().isPresent();

        if (present && bannerService.isBannerNameNotUnique(id, value.getBannerName().get())) {
            context.buildConstraintViolationWithTemplate("Указанное имя баннера уже используется")
                    .addPropertyNode("bannerName")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}