package com.example.bannerbidservice.controller;

import com.example.bannerbidservice.controller.dto.AddCategoryDTO;
import com.example.bannerbidservice.controller.dto.CategoryResponseDTO;
import com.example.bannerbidservice.controller.dto.PatchCategoryDTO;
import com.example.bannerbidservice.controller.validator.category.CategoryExistOnCreateCategory;
import com.example.bannerbidservice.controller.validator.category.patch.CategoryId;
import com.example.bannerbidservice.controller.validator.category.patch.CategoryUniqueNames;
import com.example.bannerbidservice.entity.Category;
import com.example.bannerbidservice.service.implementation.CategoryServiceImpl;
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
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    @GetMapping(path = "/search/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Category>> getBannersByNameContainingIgnoreCase(@RequestParam String partName) {
        return new ResponseEntity<>(categoryService.getCategoriesByNameContainingIgnoreCase(partName), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Category>> getActiveCategories() {
        return new ResponseEntity<>(categoryService.getActiveCategories(), HttpStatus.OK);
    }

    @PatchMapping(path = "/patch/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryResponseDTO> patchCategory(@Valid @CategoryId @PathVariable
                                                                     Long id,
                                                             @Valid
                                                             @RequestBody
                                                             @CategoryUniqueNames
                                                                     PatchCategoryDTO patchCategoryDTO,
                                                             BindingResult bindingResult) {
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();

        if (bindingResult.hasErrors()) {
            setValidationErrors(categoryResponseDTO, bindingResult);
            return new ResponseEntity<>(categoryResponseDTO, HttpStatus.CONFLICT);
        }

        categoryResponseDTO = categoryService.getCategoryResponseDTO(patchCategoryDTO, id);

        return new ResponseEntity<>(categoryResponseDTO, categoryResponseDTO.getStatus().equals("Successful") ? HttpStatus.OK : HttpStatus.CONFLICT);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid
            @CategoryExistOnCreateCategory
            @RequestBody AddCategoryDTO addCategoryDTO, BindingResult bindingResult) {
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();

        if (bindingResult.hasErrors()) {
            setValidationErrors(categoryResponseDTO, bindingResult);
            return new ResponseEntity<>(categoryResponseDTO, HttpStatus.CONFLICT);
        }

        categoryResponseDTO = categoryService.getCategoryResponseDTO(addCategoryDTO);
        return new ResponseEntity<>(categoryResponseDTO, HttpStatus.CREATED);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        HashMap<String, String> map;

        Set<ConstraintViolation<?>> set = e.getConstraintViolations();

        map = set.stream().collect(Collectors.toMap(next -> ((PathImpl) next.getPropertyPath())
                .getLeafNode().getName(), ConstraintViolation::getMessage, (a, b) -> b, HashMap::new));

        Map<String, HashMap<String, String>> errors = new HashMap<>();
        errors.put("errors", map);

        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }
}