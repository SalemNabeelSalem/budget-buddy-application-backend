package isalem.dev.budget_buddy.services;

import isalem.dev.budget_buddy.dtos.ExpenseDTO;
import isalem.dev.budget_buddy.dtos.IncomeDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final ProfileService profileService;

    private final ExpenseService expenseService;

    private final IncomeService incomeService;

    private final EmailService emailService;

    @Value("${budget-buddy.frontend.base-url}")
    private String frontEndBaseUrl;

    // for testing purposes, runs every minute -> 0 * * * * *
    // runs every day at 10 PM Dubai time -> 0 0 22 * * *
    @Scheduled(cron = "0 0 22 * * *", zone = "Asia/Dubai")
    public void sendDailyExpensesAndIncomesReport() {
        log.info("starting to send daily expenses and incomes report to all profiles.");

        profileService.getAllActivatedProfiles().forEach(profile -> {
            String body = "Hi " + profile.getFullName() + ",<br><br>"
                    + "This is a friendly reminder to add your income and expenses for today in Budget Buddy. <br><br>"
                    + "Keeping track of your finances daily can help you stay on top of your budget and achieve your financial goals.<br><br>"
                    + "<a href="+ frontEndBaseUrl +" style='display:inline-block;padding:10px 20px;background-color:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;font-weight:bold;'>Go to Budget Buddy</a>"
                    + "<br><br>Best regards,<br>Budget Buddy Team❤️"
                    + "<br><br><small style='color: #888;'>This is an automated message, please do not reply.</small>"
                    + "<br><br><small style='color: #888;'>&copy; " + java.time.Year.now().getValue() + " Budget Buddy. All rights reserved.</small>";

            String emailSubject = "Daily Reminder: Track Your Finances with Budget Buddy";

            emailService.sendEmail(profile.getEmail(), emailSubject, body);
        });

        log.info("finished sending daily expenses and incomes report to all profiles.");
    }

    // for testing purposes, runs every minute -> 0 * * * * *
    // runs every day at 11 PM Dubai time -> 0 0 23 * * *
    @Transactional
    @Scheduled(cron = "0 0 23 * * *", zone = "Asia/Dubai")
    public void sendDailyExpensesSummaryReport() {
        log.info("starting to send daily expenses summary report to all profiles.");

        profileService.getAllActivatedProfiles().forEach(profile -> {
            List<ExpenseDTO> todayExpenses = expenseService.getExpensesByDateForCurrentProfile(
                    profile.getId(), LocalDate.now()
            );

            if (!todayExpenses.isEmpty()) {
                StringBuilder table = new StringBuilder("Hi " + profile.getFullName() + ",<br><br>"
                        + "Here is a summary of your expenses for today:<br><br>");

                table.append("<table style='border-collapse:collapse;width:100%;'>");

                table.append("<tr style='background-color:#f2f2f2;'><th style='border:1px solid #ddd;padding:8px;text-align:center;'>S.No</th><th style='border:1px solid #ddd;padding:8px;'>Name</th><th style='border:1px solid #ddd;padding:8px;text-align:center;'>Amount</th><th style='border:1px solid #ddd;padding:8px;text-align:center;'>Category</th></tr>");

                int count = 1;

                for (ExpenseDTO expense : todayExpenses) {
                    table.append("<tr>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;text-align:center;'>").append(count++).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getName()).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;text-align:center;'>").append(expense.getAmount()).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;text-align:center;'>").append(expense.getCategoryId() != null ? expense.getCategoryName() : "N/A").append("</td>");
                }

                double totalAmountExpenses = todayExpenses.stream()
                        .mapToDouble(income -> income.getAmount().doubleValue())
                        .sum();

                table.append("<tr style='background-color:#f2f2f2;font-weight:bold;'><td colspan='2' style='border:1px solid #ddd;padding:8px;text-align:right;'>Total</td><td style='border:1px solid #ddd;padding:8px;text-align:center;'>").append(totalAmountExpenses).append("</td><td style='border:1px solid #ddd;padding:8px;text-align:center;'></td></tr>");

                table.append("</table><br><br>");
                table.append("Keep tracking your expenses daily to stay on top of your budget and achieve your financial goals.<br><br>");
                table.append("<a href=").append(frontEndBaseUrl).append(" style='display:inline-block;padding:10px 20px;background-color:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;font-weight:bold;'>Go to Budget Buddy</a>");
                table.append("<br><br>Best regards,<br>Budget Buddy Team❤️");
                table.append("<br><br><small style='color: #888;'>This is an automated message, please do not reply.</small>");
                table.append("<br><br><small style='color: #888;'>&copy; ").append(java.time.Year.now().getValue()).append(" Budget Buddy. All rights reserved.</small>");

                String emailSubject = "Daily Expenses Summary: Your Financial Snapshot for Today";

                emailService.sendEmail(profile.getEmail(), emailSubject, table.toString());
            }
        });

        log.info("finished sending daily expenses summary report to all profiles.");
    }

    // for testing purposes, runs every minute -> 0 * * * * *
    // runs every day at 11 PM Dubai time -> 0 0 23 * * *
    @Transactional
    @Scheduled(cron = "0 0 23 * * *", zone = "Asia/Dubai")
    public void sendDailyIncomesSummaryReport() {
        log.info("starting to send daily incomes summary report to all profiles.");

        profileService.getAllActivatedProfiles().forEach(profile -> {
            List<IncomeDTO> todayIncomes = incomeService.getIncomesByDateForCurrentProfile(
                    profile.getId(), LocalDate.now()
            );

            if (!todayIncomes.isEmpty()) {
                StringBuilder table = new StringBuilder("Hi " + profile.getFullName() + ",<br><br>"
                        + "Here is a summary of your incomes for today:<br><br>");

                table.append("<table style='border-collapse:collapse;width:100%;'>");

                table.append("<tr style='background-color:#f2f2f2;'><th style='border:1px solid #ddd;padding:8px;text-align:center;'>S.No</th><th style='border:1px solid #ddd;padding:8px;'>Name</th><th style='border:1px solid #ddd;padding:8px;text-align:center;'>Amount</th><th style='border:1px solid #ddd;padding:8px;text-align:center;'>Category</th></tr>");

                int count = 1;

                for (IncomeDTO incomes : todayIncomes) {
                    table.append("<tr>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;text-align:center;'>").append(count++).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(incomes.getName()).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;text-align:center;'>").append(incomes.getAmount()).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;text-align:center;'>").append(incomes.getCategoryId() != null ? incomes.getCategoryName() : "N/A").append("</td>");
                }

                double totalAmountIncomes = todayIncomes.stream()
                        .mapToDouble(income -> income.getAmount().doubleValue())
                        .sum();

                table.append("<tr style='background-color:#f2f2f2;font-weight:bold;'><td colspan='2' style='border:1px solid #ddd;padding:8px;text-align:right;'>Total</td><td style='border:1px solid #ddd;padding:8px;text-align:center;'>").append(totalAmountIncomes).append("</td><td style='border:1px solid #ddd;padding:8px;text-align:center;'></td></tr>");

                table.append("</table><br><br>");
                table.append("Keep tracking your incomes daily to stay on top of your budget and achieve your financial goals.<br><br>");
                table.append("<a href=").append(frontEndBaseUrl).append(" style='display:inline-block;padding:10px 20px;background-color:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;font-weight:bold;'>Go to Budget Buddy</a>");
                table.append("<br><br>Best regards,<br>Budget Buddy Team❤️");
                table.append("<br><br><small style='color: #888;'>This is an automated message, please do not reply.</small>");
                table.append("<br><br><small style='color: #888;'>&copy; ").append(java.time.Year.now().getValue()).append(" Budget Buddy. All rights reserved.</small>");

                String emailSubject = "Daily Incomes Summary: Your Financial Snapshot for Today";

                emailService.sendEmail(profile.getEmail(), emailSubject, table.toString());
            }
        });

        log.info("finished sending daily incomes summary report to all profiles.");
    }
}