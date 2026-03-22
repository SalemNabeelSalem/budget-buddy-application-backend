package isalem.dev.budget_buddy.controllers;

import isalem.dev.budget_buddy.entities.ProfileEntity;
import isalem.dev.budget_buddy.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final ExcelService excelService;

    private final EmailService emailService;

    private final IncomeService incomeService;

    private final ExpenseService expenseService;

    private final ProfileService profileService;

    @GetMapping("/excel-incomes")
    public ResponseEntity<?> sendEmailIncomesExcel() throws IOException {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        excelService.writeIncomesToExcel(outputStream, incomeService.getAllIncomesForCurrentProfileSortedByDateDesc());

        String emailBody = "<p>Dear " + currentProfile.getFullName() + ",</p>" +
                "<p>Please find attached the Excel report of your incomes.</p>" +
                "<p>Best regards,<br/>Budget Buddy Team</p>";

        emailService.sendEmailWithAttachment(
                currentProfile.getEmail(),
                "Your Incomes Excel Report",
                emailBody,
                outputStream.toByteArray(),
                "incomes.xlsx"
        );

        return ResponseEntity.status(200).body(
                Map.of("message", "Incomes Excel report has been sent to your email successfully.")
        );
    }

    @GetMapping("/excel-expenses")
    public ResponseEntity<?> sendEmailExpensesExcel() throws IOException {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        excelService.writeExpensesToExcel(outputStream, expenseService.getAllExpensesForCurrentProfileSortedByDateDesc());

        String emailBody = "<p>Dear " + currentProfile.getFullName() + ",</p>" +
                "<p>Please find attached the Excel report of your expenses.</p>" +
                "<p>Best regards,<br/>Budget Buddy Team</p>";

        emailService.sendEmailWithAttachment(
                currentProfile.getEmail(),
                "Your Expenses Excel Report",
                emailBody,
                outputStream.toByteArray(),
                "expenses.xlsx"
        );

        return ResponseEntity.status(200).body(
                Map.of("message", "Expenses Excel report has been sent to your email successfully.")
        );
    }
}