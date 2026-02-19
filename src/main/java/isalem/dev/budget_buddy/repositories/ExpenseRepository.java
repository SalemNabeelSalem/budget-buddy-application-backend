package isalem.dev.budget_buddy.repositories;

import isalem.dev.budget_buddy.entities.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long>, JpaSpecificationExecutor<ExpenseEntity> {

    /*
     * Finds all expenses by profile id ordered by date descending.
     * select * from tbl_expenses where profile_id = ?1 order by date desc
     */
    List<ExpenseEntity> findByProfileIdOrderByDateDesc(Long profileId);

    /*
     * Finds all expenses by profile id and date between start date and end date.
     * select * from tbl_expenses where profile_id = ?1 and date between ?2 and ?3 order by date desc
     */
    List<ExpenseEntity> findByProfileIdAndDateBetweenOrderByDateDesc(Long profileId, LocalDate startDate, LocalDate endDate);

    /*
     * Finds the top 5 expenses by profile id ordered by date descending.
     * select * from tbl_expenses where profile_id = ?1 order by date desc limit 5
     */
    List<ExpenseEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

    /*
     * Finds the total expenses by profile id.
     * select sum(amount) from tbl_expenses where profile_id = ?1
     */
    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.profile.id = :profileId")
    BigDecimal findTotalExpensesByProfileId(@Param("profileId") Long profileId);
}