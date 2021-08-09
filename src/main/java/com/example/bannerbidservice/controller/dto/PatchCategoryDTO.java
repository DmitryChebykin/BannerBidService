package com.example.bannerbidservice.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Optional;

@Getter
@Setter
@SuppressWarnings("ALL")

public class PatchCategoryDTO {

    private Optional<
            @NotBlank(message = "Не указано наименование категории")
            @Length(max = 255, message = "Длина наименования не должна превышать 255 символов") String>
            categoryName;

    private Optional<
            @NotBlank(message = "Не указано наименование для запросов")
            @Length(max = 255, message = "Длина наименования не должна превышать 255 символов") String>
            reqName;

    private Optional<
            @NotBlank(message = "Не указан статус категории")
            @Pattern(regexp = "^true$|^false$", message = "Статусы для категорий: true или false") String>
            deleted;
}