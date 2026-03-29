package isalem.dev.budget_buddy.repositories;

import isalem.dev.budget_buddy.entities.ExpenseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long>, JpaSpecificationExecutor<ExpenseEntity> {

    /*
     * Finds all expenses by profile id ordered by date descending.
     *
     * select * from tbl_expenses where profile_id = ?1 order by date desc
     *
     * The @EntityGraph annotation is used to specify that the category and profile associations,
     * should be fetched eagerly when executing this query. This helps to avoid the N+1 select problem
     * and improves performance by reducing the number of queries executed against the database.
     */
    @EntityGraph(attributePaths = {"category", "profile"})
    List<ExpenseEntity> findByProfileIdOrderByDateDesc(Long profileId);

    /*
     * Finds all expenses by profile id and date between start date and end date.
     *
     * select * from tbl_expenses where profile_id = ?1 and date between ?2 and ?3 order by date desc
     *
     * The @EntityGraph annotation is used to specify that the category and profile associations,
     * should be fetched eagerly when executing this query. This helps to avoid the N+1 select problem
     * and improves performance by reducing the number of queries executed against the database.
     */
    @EntityGraph(attributePaths = {"category", "profile"})
    List<ExpenseEntity> findByProfileIdAndDateBetweenOrderByDateDesc(Long profileId, LocalDate startDate, LocalDate endDate);

    /*
     * Finds the top 5 expenses by profile id ordered by date descending.
     *
     * select * from tbl_expenses where profile_id = ?1 order by date desc limit ?2
     *
     * The @EntityGraph annotation is used to specify that the category and profile associations,
     * should be fetched eagerly when executing this query. This helps to avoid the N+1 select problem
     * and improves performance by reducing the number of queries executed against the database.
     */
    @EntityGraph(attributePaths = {"category", "profile"})
    List<ExpenseEntity> findByProfileIdOrderByDateDesc(Long profileId, Pageable pageable);

    /*
     * Finds an expense by id and profile id.
     *
     * select * from tbl_expenses where id = ?1 and profile_id = ?2
     *
     * The @EntityGraph annotation is used to specify that the category and profile associations,
     * should be fetched eagerly when executing this query. This helps to avoid the N+1 select problem
     * and improves performance by reducing the number of queries executed against the database.
     */
    @EntityGraph(attributePaths = {"category", "profile"})
    Optional<ExpenseEntity> findExpenseWithCategoryAndProfileById(Long id);

    /*
     * Finds the total expenses by profile id.
     *
     * select sum(amount) from tbl_expenses where profile_id = ?1
     */
    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.profile.id = :profileId")
    BigDecimal findTotalExpensesByProfileId(@Param("profileId") Long profileId);

    /*
     * Finds all expenses by profile id and date.
     *
     * select * from tbl_expenses where profile_id = ?1 and date = ?2
     *
     * The @EntityGraph annotation is used to specify that the category and profile associations,
     * should be fetched eagerly when executing this query. This helps to avoid the N+1 select problem
     * and improves performance by reducing the number of queries executed against the database.
     */
    @EntityGraph(attributePaths = {"category", "profile"})
    List<ExpenseEntity> findByProfileIdAndDate(Long profileId, LocalDate date);
}