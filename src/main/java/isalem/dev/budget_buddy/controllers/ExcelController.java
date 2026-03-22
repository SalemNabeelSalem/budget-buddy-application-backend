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
@RequestMapping("/download")
public class ExcelController {

    private final ExcelService excelService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @GetMapping("/excel-incomes")
    public void downloadIncomesAsExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        response.setHeader("Content-Disposition", "attachment; filename=incomes.xlsx");

        excelService.writeIncomesToExcel(response.getOutputStream(), incomeService.getAllIncomesForCurrentProfileSortedByDateDesc());
    }

    @GetMapping("/excel-expenses")
    public void downloadExpensesAsExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        response.setHeader("Content-Disposition", "attachment; filename=expenses.xlsx");

        excelService.writeExpensesToExcel(response.getOutputStream(), expenseService.getAllExpensesForCurrentProfileSortedByDateDesc());
    }
}