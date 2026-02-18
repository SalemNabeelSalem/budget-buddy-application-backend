package isalem.dev.budget_buddy.controllers;

import isalem.dev.budget_buddy.dtos.ExpenseDTO;
import isalem.dev.budget_buddy.services.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/create")
    public ResponseEntity<?> createNewExpenseForCurrentProfile(@RequestBody ExpenseDTO expenseDTO) {
        try {
            ExpenseDTO newExpense = expenseService.createNewExpenseForCurrentProfile(expenseDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(newExpense);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ExpenseDTO>> findAllExpensesForCurrentProfileSortedByDateDesc() {
        List<ExpenseDTO> expenses = expenseService.getAllExpensesForCurrentProfileSortedByDateDesc();

        return ResponseEntity.status(HttpStatus.OK).body(expenses);
    }

    @GetMapping("/all-by-date-range")
    public ResponseEntity<List<ExpenseDTO>> findExpensesForCurrentProfileByDateRangeSortedByDateDesc(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        List<ExpenseDTO> expenses = expenseService.filterExpensesForCurrentProfile(startDate, endDate, null, null);

        return ResponseEntity.status(HttpStatus.OK).body(expenses);
    }

    @GetMapping("/top5")
    public ResponseEntity<List<ExpenseDTO>> findTop5ExpensesForCurrentProfileSortedByDateDesc() {
        List<ExpenseDTO> expenses = expenseService.getTop5ExpensesForCurrentProfileSortedByDateDesc();

        return ResponseEntity.status(HttpStatus.OK).body(expenses);
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> findTotalExpensesForCurrentProfile() {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.getTotalExpensesForCurrentProfile());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteExpenseByIdForCurrentProfile(@PathVariable Long id) {
        try {
            expenseService.deleteExpenseByIdForCurrentProfile(id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }
}