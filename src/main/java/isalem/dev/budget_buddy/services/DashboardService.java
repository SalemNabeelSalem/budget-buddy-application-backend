package isalem.dev.budget_buddy.services;

import isalem.dev.budget_buddy.dtos.ExpenseDTO;
import isalem.dev.budget_buddy.dtos.IncomeDTO;
import isalem.dev.budget_buddy.dtos.RecentTransactionDTO;
import isalem.dev.budget_buddy.entities.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private  final ProfileService profileService;

    private  final ExpenseService expenseService;

    private   final IncomeService incomeService;

    public Map<String, Object> getDashboardData() {
        ProfileEntity profile = profileService.getCurrentProfile();

        Map<String,Object> dashboardData = new HashMap<>();

        List<IncomeDTO> latestTop5Incomes = loadLatestTop5Incomes();

        List<ExpenseDTO> latestTop5Expenses = loadLatestTop5Expenses();

        Stream<RecentTransactionDTO> expenses = latestTop5Expenses.stream().map(
                expense -> RecentTransactionDTO.builder()
                        .id(expense.getId())
                        .profileId(profile.getId())
                        .icon(expense.getIcon())
                        .name(expense.getName())
                        .amount(expense.getAmount())
                        .date(expense.getDate())
                        .createdAt(expense.getCreatedAt())
                        .updatedAt(expense.getUpdatedAt())
                        .type("expense")
                        .build()
        );

        Stream<RecentTransactionDTO> incomes = latestTop5Incomes.stream().map(
                income -> RecentTransactionDTO.builder()
                        .id(income.getId())
                        .profileId(profile.getId())
                        .icon(income.getIcon())
                        .name(income.getName())
                        .amount(income.getAmount())
                        .date(income.getDate())
                        .createdAt(income.getCreatedAt())
                        .updatedAt(income.getUpdatedAt())
                        .type("income")
                        .build()
        );

        List<RecentTransactionDTO> recentTransactions = Stream.concat(expenses, incomes)
                .sorted((t1, t2) -> {
                    // sort by date descending, if dates are equal, sort by createdAt descending
                    int dateComparison = t2.getDate().compareTo(t1.getDate());
                    if (dateComparison != 0) {
                        return dateComparison;
                    } else {
                        return t2.getCreatedAt().compareTo(t1.getCreatedAt());
                    }
                })
                .toList();

        dashboardData.put("totalBalance", incomeService.getTotalIncomesForCurrentProfile()
                .subtract(expenseService.getTotalExpensesForCurrentProfile())
        );

        dashboardData.put("totalExpenses", expenseService.getTotalExpensesForCurrentProfile());

        dashboardData.put("totalIncomes", incomeService.getTotalIncomesForCurrentProfile());

        dashboardData.put("recent5Expenses", latestTop5Expenses);

        dashboardData.put("recent5Incomes", latestTop5Incomes);

        dashboardData.put("recentTransactions", recentTransactions);

        return dashboardData;
    }

    private List<ExpenseDTO> loadLatestTop5Expenses() {
        return expenseService.getTop5ExpensesForCurrentProfileSortedByDateDesc();
    }

    private List<IncomeDTO> loadLatestTop5Incomes() {
        return incomeService.getTop5IncomesForCurrentProfileSortedByDateDesc();
    }
}