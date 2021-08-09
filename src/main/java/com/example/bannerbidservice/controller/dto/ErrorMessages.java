package com.example.bannerbidservice.controller.dto;

import java.util.Map;

public interface ErrorMessages {
    Map<String, String> getErrorsMap();

    void setErrorsMap(Map<String, String> map);
}