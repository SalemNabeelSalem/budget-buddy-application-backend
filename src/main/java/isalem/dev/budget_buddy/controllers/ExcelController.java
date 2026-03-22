package isalem.dev.budget_buddy.controllers;

import isalem.dev.budget_buddy.services.ExcelService;
import isalem.dev.budget_buddy.services.ExpenseService;
import isalem.dev.budget_buddy.services.IncomeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/excel")
public class ExcelController {

    private final ExcelService excelService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @GetMapping("/incomes")
    public void downloadIncomesAsExcel(HttpServletResponse response) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        response.setHeader("Content-Disposition", "attachment; filename=incomes.xlsx");

        try {
            excelService.writeIncomesToExcel(response.getOutputStream(), incomeService.getAllIncomesForCurrentProfileSortedByDateDesc());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write Excel file", e);
        }
    }
}