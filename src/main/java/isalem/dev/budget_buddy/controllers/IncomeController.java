package isalem.dev.budget_buddy.controllers;

import isalem.dev.budget_buddy.dtos.IncomeDTO;
import isalem.dev.budget_buddy.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<?> createNewIncomeForCurrentProfile(@RequestBody IncomeDTO incomeDTO) {
        try {
            IncomeDTO newIncome = incomeService.createNewIncomeForCurrentProfile(incomeDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(newIncome);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @GetMapping
    public ResponseEntity<List<IncomeDTO>> findAllIncomesForCurrentProfileSortedByDateDesc() {
        List<IncomeDTO> incomes = incomeService.getAllIncomesForCurrentProfileSortedByDateDesc();

        return ResponseEntity.status(HttpStatus.OK).body(incomes);
    }

    @GetMapping("/filters")
    public ResponseEntity<List<IncomeDTO>> findAllIncomesForCurrentProfileByDateRangeSortedByDateDesc(
            @RequestParam(name="start-date", required = false) LocalDate startDate,
            @RequestParam(name="end-date", required = false) LocalDate endDate
    ) {
        List<IncomeDTO> incomes = incomeService.filterIncomesForCurrentProfile(startDate, endDate);

        return ResponseEntity.status(HttpStatus.OK).body(incomes);
    }

    @GetMapping("/top")
    public ResponseEntity<List<IncomeDTO>> findTopIncomesForCurrentProfileSortedByDateDesc(
            @RequestParam(name="limit", defaultValue = "5") int limit
    ) {
        List<IncomeDTO> incomes = incomeService.getTopIncomesForCurrentProfileSortedByDateDesc(limit);

        return ResponseEntity.status(HttpStatus.OK).body(incomes);
    }

    @GetMapping("/summary/total")
    public ResponseEntity<BigDecimal> findTotalIncomesForCurrentProfile() {
        return ResponseEntity.status(HttpStatus.OK).body(incomeService.getTotalIncomesForCurrentProfile());
    }

    @DeleteMapping("/{income-id}")
    public ResponseEntity<?> deleteIncomeByIdForCurrentProfile(@PathVariable("income-id") Long incomeId) {
        try {
            incomeService.deleteIncomeByIdForCurrentProfile(incomeId);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(
                    Map.of("message", ex.getReason() != null ? ex.getReason() : "")
            );
        }
    }
}