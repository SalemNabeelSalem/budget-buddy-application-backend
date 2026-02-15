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
public class IncomeDTO {

    private Long id; // from IncomeEntity

    private String name; // from IncomeEntity

    private String icon; // from IncomeEntity

    private LocalDate date; // from IncomeEntity

    private BigDecimal amount; // from IncomeEntity

    private Long categoryId; // from IncomeEntity -> CategoryEntity

    private String categoryName; // from IncomeEntity -> CategoryEntity

    private LocalDateTime createdAt; // from IncomeEntity

    private LocalDateTime updatedAt; // from IncomeEntity
}