package isalem.dev.budget_buddy.services;

import isalem.dev.budget_buddy.dtos.CategoryDTO;
import isalem.dev.budget_buddy.entities.CategoryEntity;
import isalem.dev.budget_buddy.entities.CategoryType;
import isalem.dev.budget_buddy.entities.ProfileEntity;
import isalem.dev.budget_buddy.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final ProfileService profileService;

    public CategoryDTO createNewCategoryForCurrentProfile(CategoryDTO categoryDTO) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        if (categoryRepository.existsByNameAndProfileId(categoryDTO.getName(), currentProfile.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with the same name already exists for this profile.");
        }

        CategoryEntity newCategoryEntity = toCategoryEntity(categoryDTO, currentProfile);

        newCategoryEntity = categoryRepository.save(newCategoryEntity);

        return toCategoryDTO(newCategoryEntity);
    }

    public List<CategoryDTO> getAllCategoriesForCurrentProfile() {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        List<CategoryEntity> categoryEntities = categoryRepository.findAllByProfileId(currentProfile.getId());

        return categoryEntities.stream()
                .map(this::toCategoryDTO)
                .toList();
    }

    public List<CategoryDTO> getAllCategoriesForCurrentProfileByType(String type) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        List<CategoryEntity> categoryEntities = categoryRepository.findAllByProfileIdAndType(currentProfile.getId(), type);

        return categoryEntities.stream()
                .map(this::toCategoryDTO)
                .toList();
    }

    public CategoryDTO updateCategoryForCurrentProfile(Long categoryId, CategoryDTO categoryDTO) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        CategoryEntity categoryEntity = categoryRepository.findByIdAndProfileId(categoryId, currentProfile.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found for the current profile."));

        // Optional, extra safeguard to ensure the category belongs to the current profile, even though we already query by profile id.
        if (!categoryEntity.getProfile().getId().equals(currentProfile.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update this category.");
        }

        if (!categoryEntity.getName().equals(categoryDTO.getName()) &&
                categoryRepository.existsByNameAndProfileId(categoryDTO.getName(), currentProfile.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with the same name already exists for this profile.");
        }

        if (categoryDTO.getType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category type is required.");
        }

        try {
            CategoryType.valueOf(categoryDTO.getType().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category type. Allowed: expense, income");
        }

        categoryEntity.setName(categoryDTO.getName());
        categoryEntity.setIcon(categoryDTO.getIcon());
        categoryEntity.setType(categoryDTO.getType());

        categoryEntity = categoryRepository.save(categoryEntity);

        return toCategoryDTO(categoryEntity);
    }

    public CategoryEntity getCategoryEntityByIdForCurrentProfile(Long categoryId) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        return categoryRepository.findByIdAndProfileId(categoryId, currentProfile.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "category not found for the current profile."));
    }

    private CategoryEntity toCategoryEntity(CategoryDTO categoryDTO, ProfileEntity profileEntity) {
        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .icon(categoryDTO.getIcon())
                .type(categoryDTO.getType())
                .profile(profileEntity)
                .build();
    }

    private CategoryDTO toCategoryDTO(CategoryEntity categoryEntity) {
        return CategoryDTO.builder()
                .id(categoryEntity.getId())
                .profileId(categoryEntity.getProfile() != null ? categoryEntity.getProfile().getId() : null)
                .name(categoryEntity.getName())
                .icon(categoryEntity.getIcon())
                .type(categoryEntity.getType())
                .createdDate(categoryEntity.getCreatedAt())
                .updatedDate(categoryEntity.getUpdatedAt())
                .build();
    }
}