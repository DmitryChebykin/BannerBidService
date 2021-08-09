package com.example.bannerbidservice.controller;

import com.example.bannerbidservice.controller.dto.AddBannerDTO;
import com.example.bannerbidservice.controller.dto.BannerResponseDTO;
import com.example.bannerbidservice.controller.dto.PatchBannerDTO;
import com.example.bannerbidservice.controller.validator.banner.BannerId;
import com.example.bannerbidservice.controller.validator.banner.BannerUniqueName;
import com.example.bannerbidservice.controller.validator.banner.CategoryExist;
import com.example.bannerbidservice.entity.Banner;
import com.example.bannerbidservice.service.implementation.BannerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static com.example.bannerbidservice.util.ErrorsHandler.setValidationErrors;

@Validated
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/banners")
public class BannerController {
    private final BannerServiceImpl bannerService;

    private final ObjectMapper objectMapper;

    @GetMapping(path = "/search_contains_ignore_case/{pageIndex}/{sizeOfPage}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Banner>> getBannersByNameContainingIgnoreCase(@RequestParam String nameParam, @PathVariable Integer pageIndex, @PathVariable Integer sizeOfPage) {
        return new ResponseEntity<>(bannerService.getBannersByNameContainingIgnoreCaseAsPageable(nameParam, pageIndex, sizeOfPage), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Banner>> getBannersByNameContainingIgnoreCase(@RequestParam String title){
        return new ResponseEntity<>(bannerService.getBannersByNameContainingIgnoreCase(title),HttpStatus.OK);
    }

    @GetMapping(path="/get_active", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Banner>> getActiveBanners() {
        return new ResponseEntity<>(bannerService.getActiveBanners(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public Banner getBanners(@Valid @BannerId @PathVariable Long id) {
        return bannerService.getBanner(id);
    }

    @GetMapping(path = "/find_all_active_pageable/{pageIndex}/{sizeOfPage}")
    @ResponseBody
    public List<Banner> getAllActiveBannersAsPageable(@PathVariable Integer pageIndex, @PathVariable Integer sizeOfPage) {
        return bannerService.getAllActiveBannerAsPageable(pageIndex, sizeOfPage);
    }

    @PostMapping(path="/new")
    public ResponseEntity<BannerResponseDTO> createBanner(@Valid @RequestBody AddBannerDTO addBannerDTO, BindingResult bindingResult) {
        BannerResponseDTO bannerResponseDTO = new BannerResponseDTO();

        if (bindingResult.hasErrors()) {
            setValidationErrors(bannerResponseDTO, bindingResult);
            return new ResponseEntity<>(bannerResponseDTO, HttpStatus.CONFLICT);
        }

        bannerResponseDTO = bannerService.getAddBannerResponseDTO(addBannerDTO);
        return new ResponseEntity<>(bannerResponseDTO, HttpStatus.CREATED);
    }

    //    https://stackoverflow.com/questions/46617124/how-to-inject-pathvariable-id-into-requestbody-before-jsr-303-validation-is-ex

    @PatchMapping(path = "/patch/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BannerResponseDTO> patchBanner(@Valid @BannerId @PathVariable
                                                                 Long id,
                                                         @Valid
                                                         @RequestBody
                                                         @BannerUniqueName
                                                         @CategoryExist
                                                                 PatchBannerDTO patchBannerDTO,
                                                         BindingResult bindingResult) {
        BannerResponseDTO bannerResponseDTO = new BannerResponseDTO();

        if (bindingResult.hasErrors()) {
            setValidationErrors(bannerResponseDTO, bindingResult);
            return new ResponseEntity<>(bannerResponseDTO, HttpStatus.BAD_REQUEST);
        }

        bannerResponseDTO = bannerService.getAddBannerResponseDTO(patchBannerDTO, id);

        return new ResponseEntity<>(bannerResponseDTO, HttpStatus.OK);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        HashMap<String, String> map;

        Set<ConstraintViolation<?>> set = e.getConstraintViolations();

        map = set.stream().collect(Collectors.toMap(next -> ((PathImpl) next.getPropertyPath())
                .getLeafNode().getName(), ConstraintViolation::getMessage, (a, b) -> b, HashMap::new));

        Map<String, HashMap<String, String>> errors = new HashMap<>();
        errors.put("errors", map);

        return new ResponseEntity<>(errors, HttpStatus.OK);
    }
}