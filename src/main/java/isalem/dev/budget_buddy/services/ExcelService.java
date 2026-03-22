package isalem.dev.budget_buddy.services;

import isalem.dev.budget_buddy.dtos.ExpenseDTO;
import isalem.dev.budget_buddy.dtos.IncomeDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class ExcelService {

    public void writeIncomesToExcel(OutputStream outputStream, List<IncomeDTO> incomes) throws IOException {
        try (Workbook workbook = new XSSFWorkbook())  {
            Sheet sheet = workbook.createSheet("Incomes");

            // Create header row
            Row headerRow = sheet.createRow(0);

            headerRow.createCell(0).setCellValue("S.No");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Date");
            headerRow.createCell(3).setCellValue("Amount");
            headerRow.createCell(4).setCellValue("Category");

            // Fill data rows
            int rowNum = 1;

            for (IncomeDTO income : incomes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1); // S.No
                row.createCell(1).setCellValue(income.getName() != null ? income.getName() : "N/A"); // Name
                row.createCell(2).setCellValue(income.getDate() != null ? income.getDate().toString() : "N/A"); // Date
                row.createCell(3).setCellValue(income.getAmount() != null ? income.getAmount().doubleValue() : 0.0); // Amount
                row.createCell(4).setCellValue(income.getCategoryName() != null ? income.getCategoryName() : "N/A"); // Category
            }

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write incomes to Excel file", e);
        }
    }

    public void writeExpensesToExcel(OutputStream outputStream, List<ExpenseDTO> expenses) throws IOException {
        try (Workbook workbook = new XSSFWorkbook())  {
            Sheet sheet = workbook.createSheet("Expenses");

            // Create header row
            Row headerRow = sheet.createRow(0);

            headerRow.createCell(0).setCellValue("S.No");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Date");
            headerRow.createCell(3).setCellValue("Amount");
            headerRow.createCell(4).setCellValue("Category");

            // Fill data rows
            int rowNum = 1;

            for (ExpenseDTO expense : expenses) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1); // S.No
                row.createCell(1).setCellValue(expense.getName() != null ? expense.getName() : "N/A"); // Name
                row.createCell(2).setCellValue(expense.getDate() != null ? expense.getDate().toString() : "N/A"); // Date
                row.createCell(3).setCellValue(expense.getAmount() != null ? expense.getAmount().doubleValue() : 0.0); // Amount
                row.createCell(4).setCellValue(expense.getCategoryName() != null ? expense.getCategoryName() : "N/A"); // Category
            }

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write expenses to Excel file", e);
        }
    }
}