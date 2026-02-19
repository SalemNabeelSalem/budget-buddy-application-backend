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

    private String name; // to search in the name of the transaction

    private String sortBy; // "date" or "amount" or "name"

    private LocalDate startDate;

    private LocalDate endDate;

    private String direction; // "asc" or "desc"
}