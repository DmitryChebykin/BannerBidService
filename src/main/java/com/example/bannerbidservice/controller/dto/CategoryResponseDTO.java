package com.example.bannerbidservice.controller.dto;

import com.example.bannerbidservice.entity.Banner;
import com.example.bannerbidservice.entity.Category;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class CategoryResponseDTO implements ErrorMessages {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> messages = new ArrayList<String>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Long> activeBannersId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Category category;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errorsMap;

    public void addMessage(String message) {
        messages.add(message);
    }
}