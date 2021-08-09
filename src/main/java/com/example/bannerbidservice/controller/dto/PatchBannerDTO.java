package com.example.bannerbidservice.controller.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("ALL")

public class PatchBannerDTO {
    private Optional<
            @NotBlank(message = "Не указано наименование баннера")
            @Length(max = 255, message = "Длина наименования не должна превышать 255 символов") String>
            bannerName;

    private Optional<
            @DecimalMin(value = "0.01", message = "Слишком малая стоимость")
            @DecimalMax(value = "9999", message = "Слишком большая стоимость")
            @Digits(integer = 4, fraction = 2, message = "Неверный формат стоимости")
            @Positive(message = "Стоимость должна быть положительной и не равной 0")
            @NotNull(message = "Не указана стоимость баннера") BigDecimal>
            price;

    private Optional<
            @NotBlank(message = "Не указана категория баннера")
            @Length(max = 255, message = "Длина категории не должна превышать 255 символов") String>
            categoryName;

    private Optional<
            @NotBlank(message = "Необходимо задать содержимое баннера")
            @Length(max = 65535) String>
            content;

    private Optional<
            @NotBlank(message = "Не указан статус баннера")
            @Pattern(regexp = "^true$|^false$", message = "Статусы для баннеров: true или false") String>
            deleted;

    @Override
    public String toString() {
        return "com.example.bannerbidservice.controller.PatchBannerDTO{" +
                "bannerName=" + bannerName +
                ", category=" + categoryName +
                ", content=" + content +
                ", deleted=" + deleted +
                ", price=" + price +
                '}';
    }
}