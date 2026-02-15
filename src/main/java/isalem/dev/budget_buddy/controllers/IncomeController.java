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

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping("/create")
    public ResponseEntity<?> createNewIncomeForCurrentProfile(@RequestBody IncomeDTO incomeDTO) {
        try {
            IncomeDTO newIncome = incomeService.createNewIncomeForCurrentProfile(incomeDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(newIncome);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<IncomeDTO>> findAllIncomesForCurrentProfileSortedByDateDesc() {
        List<IncomeDTO> incomes = incomeService.getAllIncomesForCurrentProfileSortedByDateDesc();

        return ResponseEntity.status(HttpStatus.OK).body(incomes);
    }

    @GetMapping("/all-by-date-range")
    public ResponseEntity<List<IncomeDTO>> findAllIncomesForCurrentProfileByDateRangeSortedByDateDesc(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        List<IncomeDTO> incomes = incomeService.getAllIncomesForCurrentProfileByDateRangeSortedByDateDesc(startDate, endDate);

        return ResponseEntity.status(HttpStatus.OK).body(incomes);
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> findTotalIncomesForCurrentProfile() {
        return ResponseEntity.status(HttpStatus.OK).body(incomeService.getTotalIncomesForCurrentProfile());
    }
}