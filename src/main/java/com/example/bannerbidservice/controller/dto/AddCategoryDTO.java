package com.example.bannerbidservice.controller.dto;

import com.example.bannerbidservice.entity.Category;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class AddCategoryDTO {
    @NotBlank(message = "Не указано наименование категории")
    @Length(max = 255, message = "Длина наименования не должна превышать 255 символов")
    private String categoryName;

    @NotBlank(message = "Не указано наименование для запросов")
    @Length(max = 255, message = "Длина наименования не должна превышать 255 символов")
    private String reqName;

    @NotBlank(message = "Не указан статус категории")
    @Pattern(regexp = "^true$|^false$", message = "Статусы для категорий: true или false")
    private String deleted;

    public Category toCategory() {
        return Category.CategoryBuilder.aCategory()
                .name(categoryName)
                .reqName(reqName)
                .deleted(Boolean.valueOf(deleted))
                .build();
    }
}