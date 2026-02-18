package isalem.dev.budget_buddy.controllers;

import isalem.dev.budget_buddy.dtos.ExpenseDTO;
import isalem.dev.budget_buddy.dtos.FilterDTO;
import isalem.dev.budget_buddy.dtos.IncomeDTO;
import isalem.dev.budget_buddy.services.ExpenseService;
import isalem.dev.budget_buddy.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filters")
public class FilterController {

    private final ExpenseService expenseService;

    private final IncomeService incomeService;

    @PostMapping("/transactions")
    public ResponseEntity<?> filterTransactionsForCurrentProfile(@RequestBody FilterDTO filterDTO) {
        LocalDate startDate = filterDTO.getStartDate() != null ? filterDTO.getStartDate() : LocalDate.MIN;

        LocalDate endDate = filterDTO.getEndDate() != null ? filterDTO.getEndDate() : LocalDate.now();

        String keyword = filterDTO.getKeyword() != null ? filterDTO.getKeyword() : "";

        String sortField = filterDTO.getSortField() != null ? filterDTO.getSortField() : "date";

        Sort.Direction direction = "desc".equalsIgnoreCase(filterDTO.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortField);

        if ("expense".equalsIgnoreCase(filterDTO.getType())) {
            List<ExpenseDTO> filteredExpenses = expenseService.filterExpensesForCurrentProfile(startDate, endDate, keyword, sort);

            return ResponseEntity.status(200).body(filteredExpenses);
        } else if ("income".equalsIgnoreCase(filterDTO.getType())) {
            List<IncomeDTO> filteredIncomes = incomeService.filterIncomesForCurrentProfile(startDate, endDate, keyword, sort);

            return ResponseEntity.status(200).body(filteredIncomes);
        } else {
            return ResponseEntity.badRequest().body("invalid transaction type. must be 'expense' or 'income'.");
        }
    }
}