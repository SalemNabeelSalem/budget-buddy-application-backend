package isalem.dev.budget_buddy.services;

import isalem.dev.budget_buddy.dtos.IncomeDTO;
import isalem.dev.budget_buddy.entities.CategoryEntity;
import isalem.dev.budget_buddy.entities.IncomeEntity;
import isalem.dev.budget_buddy.entities.ProfileEntity;
import isalem.dev.budget_buddy.repositories.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;

    private final CategoryService categoryService;

    private final ProfileService profileService;

    public IncomeDTO createNewIncomeForCurrentProfile(IncomeDTO incomeDTO) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        CategoryEntity categoryEntity;

        if (incomeDTO.getCategoryId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "category id is required.");
        }

        categoryEntity = categoryService.getCategoryEntityByIdForCurrentProfile(incomeDTO.getCategoryId());

        if (!categoryEntity.getType().equals("income")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "the specified category is not of type income.");
        }

        IncomeEntity newIncomeEntity = toIncomeEntity(incomeDTO, categoryEntity, currentProfile);

        newIncomeEntity = incomeRepository.save(newIncomeEntity);

        return toIncomeDTO(newIncomeEntity);
    }

    public List<IncomeDTO> getAllIncomesForCurrentProfileSortedByDateDesc() {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        List<IncomeEntity> incomeEntities = incomeRepository.findByProfileIdOrderByDateDesc(currentProfile.getId());

        return incomeEntities.stream()
                .map(this::toIncomeDTO)
                .toList();
    }

    public List<IncomeDTO> getIncomesForCurrentProfile(String name, LocalDate startDate, LocalDate endDate) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        LocalDate defaultStartDate = (startDate == null || startDate.toString().isEmpty())
                ? LocalDate.now().withDayOfMonth(1)
                : startDate;

        LocalDate defaultEndDate = (endDate == null || endDate.toString().isEmpty())
                ? LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())
                : endDate;


        List<IncomeEntity> incomeEntities;

        if (name == null || name.isEmpty()) {
            incomeEntities = incomeRepository.findByProfileIdAndDateBetweenOrderByDateDesc(
                    currentProfile.getId(),
                    defaultStartDate,
                    defaultEndDate
            );
        } else {
            incomeEntities = incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCaseOrderByDateDesc(
                    currentProfile.getId(),
                    defaultStartDate,
                    defaultEndDate,
                    name
            );
        }

        return incomeEntities.stream()
                .map(this::toIncomeDTO)
                .toList();
    }

    /**
    public List<IncomeDTO> getAllIncomesForCurrentProfileByDateRangeSortedByDateDesc(LocalDate startDate, LocalDate endDate) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        if (startDate == null || startDate.toString().isEmpty()) {
            startDate = LocalDate.now().withDayOfMonth(1);
        }

        if (endDate == null || endDate.toString().isEmpty()) {
            endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        }

        List<IncomeEntity> incomeEntities = incomeRepository.findByProfileIdAndDateBetweenOrderByDateDesc(
                currentProfile.getId(),
                startDate,
                endDate
        );

        return incomeEntities.stream()
                .map(this::toIncomeDTO)
                .toList();
    }

    public List<IncomeDTO> searchIncomesForCurrentProfileByNameAndDateRangeSortedByDateDesc(String name, LocalDate startDate, LocalDate endDate) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        if (startDate == null || startDate.toString().isEmpty()) {
            startDate = LocalDate.now().withDayOfMonth(1);
        }

        if (endDate == null || endDate.toString().isEmpty()) {
            endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        }

        List<IncomeEntity> incomeEntities = incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCaseOrderByDateDesc(
                currentProfile.getId(),
                startDate,
                endDate,
                name
        );

        return incomeEntities.stream()
                .map(this::toIncomeDTO)
                .toList();
    }
    */

    public List<IncomeDTO> getTop5IncomesForCurrentProfileSortedByDateDesc() {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        List<IncomeEntity> incomeEntities = incomeRepository.findTop5ByProfileIdOrderByDateDesc(currentProfile.getId());

        return incomeEntities.stream()
                .map(this::toIncomeDTO)
                .toList();
    }

    public BigDecimal getTotalIncomesForCurrentProfile() {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        BigDecimal total = incomeRepository.findTotalIncomesByProfileId(currentProfile.getId());

        return total != null ? total : BigDecimal.ZERO;
    }

    public void deleteIncomeByIdForCurrentProfile(Long incomeId) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        IncomeEntity incomeEntity = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "income not found with id: " + incomeId));

        if (!incomeEntity.getProfile().getId().equals(currentProfile.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you are not unauthorized to delete this income.");
        }

        incomeRepository.delete(incomeEntity);
    }

    private IncomeEntity toIncomeEntity(IncomeDTO incomeDTO, CategoryEntity categoryEntity, ProfileEntity profileEntity) {
        return IncomeEntity.builder()
                .id(incomeDTO.getId())
                .name(incomeDTO.getName())
                .icon(incomeDTO.getIcon())
                .date(incomeDTO.getDate())
                .amount(incomeDTO.getAmount())
                .category(categoryEntity)
                .profile(profileEntity)
                .build();
    }

    private IncomeDTO toIncomeDTO(IncomeEntity incomeEntity) {
        return IncomeDTO.builder()
                .id(incomeEntity.getId())
                .name(incomeEntity.getName())
                .icon(incomeEntity.getIcon())
                .date(incomeEntity.getDate())
                .amount(incomeEntity.getAmount())
                .categoryId(incomeEntity.getCategory() != null ? incomeEntity.getCategory().getId() : null)
                .categoryName(incomeEntity.getCategory() != null ? incomeEntity.getCategory().getName() : "N/A")
                .createdAt(incomeEntity.getCreatedAt())
                .updatedAt(incomeEntity.getUpdatedAt())
                .build();
    }
}