package isalem.dev.budget_buddy.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {

    private Long id; // from ExpenseEntity

    private String name; // from ExpenseEntity

    private String icon; // from ExpenseEntity

    private LocalDate date; // from ExpenseEntity

    private BigDecimal amount; // from ExpenseEntity

    private Long categoryId; // from ExpenseEntity -> CategoryEntity

    private String categoryName; // from ExpenseEntity -> CategoryEntity

    private LocalDateTime createdAt; // from ExpenseEntity

    private LocalDateTime updatedAt; // from ExpenseEntity
}