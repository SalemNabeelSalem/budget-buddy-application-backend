package isalem.dev.budget_buddy.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterDTO {

    private String type; // "expense" or "income"

    private LocalDate startDate;

    private LocalDate endDate;

    private String keyword;

    private String sortField; // "date" or "amount" or "name"

    private String sortOrder; // "asc" or "desc"
}