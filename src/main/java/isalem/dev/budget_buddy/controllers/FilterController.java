package isalem.dev.budget_buddy.controllers;

import isalem.dev.budget_buddy.dtos.FilterDTO;
import isalem.dev.budget_buddy.services.ExpenseService;
import isalem.dev.budget_buddy.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filters")
public class FilterController {

    private final ExpenseService expenseService;

    private final IncomeService incomeService;

    @PostMapping("/expenses")
    public ResponseEntity<?> filterExpensesForCurrentProfile(@RequestBody FilterDTO filterDTO) {

        return ResponseEntity.status(200).body(expenseService.getFilteredExpensesForCurrentProfile(filterDTO));
    }

    @PostMapping("/incomes")
    public ResponseEntity<?> filterIncomesForCurrentProfile(@RequestBody FilterDTO filterDTO) {
        return ResponseEntity.status(200).body(incomeService.getFilteredIncomesForCurrentProfile(filterDTO));
    }
}