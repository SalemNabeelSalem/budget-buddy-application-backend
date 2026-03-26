package isalem.dev.budget_buddy.controllers;

import isalem.dev.budget_buddy.dtos.FilterDTO;
import isalem.dev.budget_buddy.services.ExpenseService;
import isalem.dev.budget_buddy.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filters")
public class FilterController {

    private final ExpenseService expenseService;

    private final IncomeService incomeService;

    @PostMapping()
    public ResponseEntity<?> filterExpensesAndIncomesForCurrentProfile(@RequestBody FilterDTO filterDTO) {
        if (filterDTO.getType().equalsIgnoreCase("expense")) {
            return ResponseEntity.status(200).body(expenseService.getFilteredExpensesForCurrentProfile(filterDTO));
        } else if (filterDTO.getType().equalsIgnoreCase("income")) {
            return ResponseEntity.status(200).body(incomeService.getFilteredIncomesForCurrentProfile(filterDTO));
        } else {
            return ResponseEntity.status(400).body(
                    Map.of("message", "Invalid type. Type must be either 'expense' or 'income'.")
            );
        }
    }
}