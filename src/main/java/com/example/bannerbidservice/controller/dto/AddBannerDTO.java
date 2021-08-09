package com.example.bannerbidservice.controller.dto;

import com.example.bannerbidservice.entity.Banner;
import com.example.bannerbidservice.entity.Category;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Setter
@Getter
public class AddBannerDTO {
    @NotBlank(message = "Не указано наименование баннера")
    @Length(max = 255, message = "Длина наименования не должна превышать 255 символов")
    private String bannerName;

    @DecimalMin(value = "0.01", message = "Слишком малая стоимость")
    @DecimalMax(value = "9999", message = "Слишком большая стоимость")
    @Digits(integer = 4, fraction = 2, message = "Неверный формат стоимости")
    @Positive(message = "Стоимость должна быть положительной")
    @NotNull(message = "Не указана стоимость баннера")
    private BigDecimal price;

    @NotBlank(message = "Не указана категория баннера")
    @Length(max = 255, message = "Длина категории не должна превышать 255 символов")
    private String categoryName;

    @NotBlank(message = "Необходимо задать содержимое баннера")
    @Length(max = 65535)
    private String content;

    @NotBlank(message = "Не указан статус баннера")
    @Pattern(regexp = "^true$|^false$", message = "Статусы для баннеров: true или false")
    private String deleted;

    public Banner toBanner() {
        return Banner.BannerBuilder.aBanner()
                .bannerName(bannerName)
                .category(Category.CategoryBuilder.aCategory().name(categoryName).build())
                .price(price)
                .content(content)
                .deleted(Boolean.valueOf(deleted))
                .build();
    }
}