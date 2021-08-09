package com.example.bannerbidservice.controller;

import com.example.bannerbidservice.service.implementation.AdsRequestServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@RestController
@AllArgsConstructor
@RequestMapping("/bid")
public class AdsRequestController {
    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"};

    @Resource
    private final AdsRequestServiceImpl adsRequestService;
    @Resource
    private final HttpServletRequest request;

    private static String getClientIpAddress(HttpServletRequest request) {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);

            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    @PostMapping(path = "add_request/")
    public Long addRequest(String categoryReq, String ipAddress, String userAgent) {
        return adsRequestService.addRequest(categoryReq, ipAddress, userAgent);
    }

    @GetMapping()
    public ResponseEntity<String> getBannerContent(@RequestParam String category) {
        String bannerText = adsRequestService.getBannerContent(category, getClientIpAddress(request), request.getHeader("User-Agent")).orElse(null);
        if (bannerText != null) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));
            return new ResponseEntity<>(bannerText, httpHeaders, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}