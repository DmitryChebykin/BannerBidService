package com.example.bannerbidservice.controller.dto;

import com.example.bannerbidservice.entity.Banner;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class BannerResponseDTO implements ErrorMessages {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> messages = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Banner banner;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errorsMap;

    public void addMessage(String message) {
        messages.add(message);
    }
}