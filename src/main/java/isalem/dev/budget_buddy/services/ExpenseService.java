package isalem.dev.budget_buddy.services;

import isalem.dev.budget_buddy.dtos.ExpenseDTO;
import isalem.dev.budget_buddy.dtos.FilterDTO;
import isalem.dev.budget_buddy.entities.CategoryEntity;
import isalem.dev.budget_buddy.entities.ExpenseEntity;
import isalem.dev.budget_buddy.entities.ProfileEntity;
import isalem.dev.budget_buddy.repositories.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "the specified category is not of type expense.");
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

    public List<ExpenseDTO> getTop5ExpensesForCurrentProfileSortedByDateDesc() {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        List<ExpenseEntity> expenseEntities = expenseRepository.findTop5ByProfileIdOrderByDateDesc(currentProfile.getId());

        return expenseEntities.stream()
                .map(this::toExpenseDTO)
                .toList();
    }

    public List<ExpenseDTO> getFilteredExpensesForCurrentProfile(FilterDTO filterDTO) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        Sort sort = filterDTO.getDirection().equalsIgnoreCase("desc")
                ? Sort.by(filterDTO.getSortBy()).descending()
                : Sort.by(filterDTO.getSortBy()).ascending();

        Specification<ExpenseEntity> specification = (root, query, criteriaBuilder)
                -> criteriaBuilder.conjunction();

        // ensure we only get expenses for the current profile
        specification = specification.and(specification)
                .and((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("profile").get("id"), currentProfile.getId())
                );

        // filter by name
        if (filterDTO.getName() != null && !filterDTO.getName().isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filterDTO.getName().toLowerCase() + "%")
            );
        }

        // filter by date range
        if (filterDTO.getStartDate() != null && filterDTO.getEndDate() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("date"), filterDTO.getStartDate(), filterDTO.getEndDate())
            );
        }

        return expenseRepository.findAll(specification, sort).stream()
                .map(this::toExpenseDTO)
                .toList();
    }

    public List<ExpenseDTO> filterExpensesForCurrentProfile(LocalDate startDate, LocalDate endDate) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        LocalDate defaultStartDate = (startDate == null || startDate.toString().isEmpty())
                ? LocalDate.now().withDayOfMonth(1)
                : startDate;

        LocalDate defaultEndDate = (endDate == null || endDate.toString().isEmpty())
                ? LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())
                : endDate;

        List<ExpenseEntity> expenseEntities;

        expenseEntities = expenseRepository.findByProfileIdAndDateBetweenOrderByDateDesc(
                currentProfile.getId(),
                defaultStartDate,
                defaultEndDate
        );

        return expenseEntities.stream()
                .map(this::toExpenseDTO)
                .toList();
    }

    public BigDecimal getTotalExpensesForCurrentProfile() {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        BigDecimal total = expenseRepository.findTotalExpensesByProfileId(currentProfile.getId());

        return total != null ? total : BigDecimal.ZERO;
    }

    public void deleteExpenseByIdForCurrentProfile(Long expenseId) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        ExpenseEntity expenseEntity = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "expense not found with id: " + expenseId));

        if (!expenseEntity.getProfile().getId().equals(currentProfile.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you are not unauthorized to delete this expense.");
        }

        expenseRepository.delete(expenseEntity);
    }

    public List<ExpenseDTO> getExpensesByDateForCurrentProfile(Long profileId, LocalDate date) {
        List<ExpenseEntity> expenseEntities = expenseRepository.findByProfileIdAndDate(profileId, date);

        return expenseEntities.stream()
                .map(this::toExpenseDTO)
                .toList();
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