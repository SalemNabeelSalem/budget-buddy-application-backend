package isalem.dev.budget_buddy.controllers;

import isalem.dev.budget_buddy.entities.ProfileEntity;
import isalem.dev.budget_buddy.services.EmailService;
import isalem.dev.budget_buddy.services.ExcelService;
import isalem.dev.budget_buddy.services.IncomeService;
import isalem.dev.budget_buddy.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final ExcelService excelService;

    private final EmailService emailService;

    private final IncomeService incomeService;

    private final ExcelService expenseExcelService;

    private final ProfileService profileService;

    @GetMapping("/incomes-excel")
    public ResponseEntity<Void> sendEmailIncomesExcel() throws IOException {
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

        return ResponseEntity.status(200).build();
    }
}