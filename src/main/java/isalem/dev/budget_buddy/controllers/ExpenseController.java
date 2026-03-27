package isalem.dev.budget_buddy.controllers;

import isalem.dev.budget_buddy.dtos.ExpenseDTO;
import isalem.dev.budget_buddy.services.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<?> createNewExpenseForCurrentProfile(@RequestBody ExpenseDTO expenseDTO) {
        try {
            ExpenseDTO newExpense = expenseService.createNewExpenseForCurrentProfile(expenseDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(newExpense);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> findAllExpensesForCurrentProfileSortedByDateDesc() {
        List<ExpenseDTO> expenses = expenseService.getAllExpensesForCurrentProfileSortedByDateDesc();

        return ResponseEntity.status(HttpStatus.OK).body(expenses);
    }

    @GetMapping("/filters")
    public ResponseEntity<List<ExpenseDTO>> findExpensesForCurrentProfileByDateRangeSortedByDateDesc(
            @RequestParam(name = "start-date", required = false) LocalDate startDate,
            @RequestParam(name = "end-date", required = false) LocalDate endDate
    ) {
        List<ExpenseDTO> expenses = expenseService.filterExpensesForCurrentProfile(startDate, endDate);

        return ResponseEntity.status(HttpStatus.OK).body(expenses);
    }

    @GetMapping("/top")
    public ResponseEntity<List<ExpenseDTO>> findTopExpensesForCurrentProfileSortedByDateDesc(
            @RequestParam(name = "limit", defaultValue = "5") int limit
    ) {
        List<ExpenseDTO> expenses = expenseService.getTopExpensesForCurrentProfileSortedByDateDesc(limit);

        return ResponseEntity.status(HttpStatus.OK).body(expenses);
    }

    // This endpoint generated the same result on the frontend side.
    @GetMapping("/summary/total")
    public ResponseEntity<?> findTotalExpensesForCurrentProfile() {
        return ResponseEntity.status(HttpStatus.OK).body(
                Map.of("totalExpensesAmount", expenseService.getTotalExpensesForCurrentProfile())
        );
    }

    @PutMapping("/{expense-id}")
    public ResponseEntity<?> updateExpenseByIdForCurrentProfile(
            @PathVariable("expense-id") Long expenseId,
            @RequestBody ExpenseDTO expenseDTO
    ) {
        try {
            ExpenseDTO updatedExpense = expenseService.updateExpenseByIdForCurrentProfile(expenseId, expenseDTO);

            return ResponseEntity.status(HttpStatus.OK).body(updatedExpense);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(
                    Map.of("message", ex.getReason() != null ? ex.getReason() : "")
            );
        }
    }

    @DeleteMapping("/{expense-id}")
    public ResponseEntity<?> deleteExpenseByIdForCurrentProfile(@PathVariable("expense-id") Long expenseId) {
        try {
            expenseService.deleteExpenseByIdForCurrentProfile(expenseId);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(
                    Map.of("message", ex.getReason() != null ? ex.getReason() : "")
            );
        }
    }
}