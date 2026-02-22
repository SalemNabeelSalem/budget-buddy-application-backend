package isalem.dev.budget_buddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BudgetBuddyApplication {

	public static void main(String[] args) {

		SpringApplication.run(BudgetBuddyApplication.class, args);
	}
}