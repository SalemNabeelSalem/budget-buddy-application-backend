package isalem.dev.budget_buddy.services;

import isalem.dev.budget_buddy.dtos.ExpenseDTO;
import isalem.dev.budget_buddy.entities.CategoryEntity;
import isalem.dev.budget_buddy.entities.ExpenseEntity;
import isalem.dev.budget_buddy.entities.ProfileEntity;
import isalem.dev.budget_buddy.repositories.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final CategoryService categoryService;

    private final ProfileService profileService;

    public ExpenseDTO createNewExpenseForCurrentProfile(ExpenseDTO expenseDTO) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        CategoryEntity categoryEntity;

        if (expenseDTO.getCategoryId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "category id is required.");
        }

        categoryEntity = categoryService.getCategoryEntityByIdForCurrentProfile(expenseDTO.getCategoryId());

        if (!categoryEntity.getType().equals("expense")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified category is not of type expense.");
        }

        ExpenseEntity newExpenseEntity = toExpenseEntity(expenseDTO, categoryEntity, currentProfile);

        newExpenseEntity = expenseRepository.save(newExpenseEntity);

        return toExpenseDTO(newExpenseEntity);
    }

    public List<ExpenseDTO> getAllExpensesForCurrentProfileSortedByDateDesc() {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        List<ExpenseEntity> expenseEntities = expenseRepository.findByProfileIdOrderByDateDesc(currentProfile.getId());

        return expenseEntities.stream()
                .map(this::toExpenseDTO)
                .toList();
    }

    public List<ExpenseDTO> getExpensesForCurrentProfile(String name, LocalDate startDate, LocalDate endDate) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        LocalDate defaultStartDate = (startDate == null || startDate.toString().isEmpty())
                ? LocalDate.now().withDayOfMonth(1)
                : startDate;

        LocalDate defaultEndDate = (endDate == null || endDate.toString().isEmpty())
                ? LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())
                : endDate;


        List<ExpenseEntity> expenseEntities;

        if (name == null || name.isEmpty()) {
            expenseEntities = expenseRepository.findByProfileIdAndDateBetweenOrderByDateDesc(
                    currentProfile.getId(),
                    defaultStartDate,
                    defaultEndDate
            );
        } else {
            expenseEntities = expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCaseOrderByDateDesc(
                    currentProfile.getId(),
                    defaultStartDate,
                    defaultEndDate,
                    name
            );
        }

        return expenseEntities.stream()
                .map(this::toExpenseDTO)
                .toList();
    }

    /**
    public List<ExpenseDTO> getExpensesForCurrentProfileByDateRangeSortedByDateDesc(LocalDate startDate, LocalDate endDate) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        if (startDate == null || startDate.toString().isEmpty()) {
            startDate = LocalDate.now().withDayOfMonth(1);
        }

        if (endDate == null || endDate.toString().isEmpty()) {
            endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        }

        List<ExpenseEntity> expenseEntities = expenseRepository.findByProfileIdAndDateBetweenOrderByDateDesc(
                currentProfile.getId(),
                startDate,
                endDate
        );

        return expenseEntities.stream()
                .map(this::toExpenseDTO)
                .toList();
    }

    public List<ExpenseDTO> searchExpensesForCurrentProfileByNameAndDateRangeSortedByDateDesc(String name, LocalDate startDate, LocalDate endDate) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        if (startDate == null || startDate.toString().isEmpty()) {
            startDate = LocalDate.now().withDayOfMonth(1);
        }

        if (endDate == null || endDate.toString().isEmpty()) {
            endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        }

        List<ExpenseEntity> expenseEntities = expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCaseOrderByDateDesc(
                currentProfile.getId(),
                startDate,
                endDate,
                name
        );

        return expenseEntities.stream()
                .map(this::toExpenseDTO)
                .toList();
    }
    */

    public BigDecimal getTotalExpensesForCurrentProfile() {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        return expenseRepository.findTotalExpensesByProfileId(currentProfile.getId());
    }

    private ExpenseEntity toExpenseEntity(ExpenseDTO expenseDTO, CategoryEntity categoryEntity, ProfileEntity profileEntity) {
        return ExpenseEntity.builder()
                .id(expenseDTO.getId())
                .name(expenseDTO.getName())
                .icon(expenseDTO.getIcon())
                .date(expenseDTO.getDate())
                .amount(expenseDTO.getAmount())
                .category(categoryEntity)
                .profile(profileEntity)
                .build();
    }

    private ExpenseDTO toExpenseDTO(ExpenseEntity expenseEntity) {
        return ExpenseDTO.builder()
                .id(expenseEntity.getId())
                .name(expenseEntity.getName())
                .icon(expenseEntity.getIcon())
                .date(expenseEntity.getDate())
                .amount(expenseEntity.getAmount())
                .categoryId(expenseEntity.getCategory() != null ? expenseEntity.getCategory().getId() : null)
                .categoryName(expenseEntity.getCategory() != null ? expenseEntity.getCategory().getName() : "N/A")
                .createdAt(expenseEntity.getCreatedAt())
                .updatedAt(expenseEntity.getUpdatedAt())
                .build();
    }
}