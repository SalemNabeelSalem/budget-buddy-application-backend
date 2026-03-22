package isalem.dev.budget_buddy.services;

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
                row.createCell(1).setCellValue(income.getName());  // Name
                row.createCell(2).setCellValue(income.getDate().toString()); // Date
                row.createCell(3).setCellValue(income.getAmount().doubleValue()); // Amount
                row.createCell(4).setCellValue(income.getCategoryName()); // Category
            }

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write Excel file", e);
        }
    }
}