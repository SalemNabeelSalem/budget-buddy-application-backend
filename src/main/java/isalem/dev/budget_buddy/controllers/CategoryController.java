package isalem.dev.budget_buddy.controllers;

import isalem.dev.budget_buddy.dtos.CategoryDTO;
import isalem.dev.budget_buddy.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> createNewCategoryForCurrentProfile(@RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO newCategory = categoryService.createNewCategoryForCurrentProfile(categoryDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(
                    Map.of("message", ex.getReason() != null ? ex.getReason() : "")
            );
        }
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategoriesForCurrentProfile(
            @RequestParam(required = false) String type
    ) {
        // If type is null, get all categories for current profile, otherwise get categories by type for current profile.
        List<CategoryDTO> categories = (type == null) ?
                categoryService.getAllCategoriesForCurrentProfile() :
                categoryService.getAllCategoriesForCurrentProfileByType(type);

        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @PutMapping("/{category-id}")
    public ResponseEntity<?> updateCategoryForCurrentProfile(
            @PathVariable("category-id") Long categoryId,
            @RequestBody CategoryDTO categoryDTO
    ) {
        try {
            CategoryDTO updatedCategory = categoryService.updateCategoryForCurrentProfile(categoryId, categoryDTO);

            return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(
                    Map.of("message", ex.getReason() != null ? ex.getReason() : "")
            );
        }
    }
}