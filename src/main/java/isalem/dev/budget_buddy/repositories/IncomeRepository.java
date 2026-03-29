package isalem.dev.budget_buddy.repositories;

import isalem.dev.budget_buddy.entities.IncomeEntity;
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

public interface IncomeRepository extends JpaRepository<IncomeEntity, Long>, JpaSpecificationExecutor<IncomeEntity> {

    /*
     * Finds all incomes by profile id ordered by date descending.
     *
     * select * from tbl_incomes where profile_id = ?1 order by date desc
     *
     * The @EntityGraph annotation is used to specify that the category and profile associations,
     * should be fetched eagerly when executing this query. This helps to avoid the N+1 select problem
     * and improves performance by reducing the number of queries executed against the database.
     */
    @EntityGraph(attributePaths = {"category", "profile"})
    List<IncomeEntity> findByProfileIdOrderByDateDesc(Long profileId);

    /*
     * Finds all incomes by profile id and date between start date and end date.
     *
     * select * from tbl_incomes where profile_id = ?1 and date between ?2 and ?3 order by date desc
     *
     * The @EntityGraph annotation is used to specify that the category and profile associations,
     * should be fetched eagerly when executing this query. This helps to avoid the N+1 select problem
     * and improves performance by reducing the number of queries executed against the database.
     */
    @EntityGraph(attributePaths = {"category", "profile"})
    List<IncomeEntity> findByProfileIdAndDateBetweenOrderByDateDesc(Long profileId, LocalDate startDate, LocalDate endDate);

    /*
     * Finds the top 5 incomes by profile id ordered by date descending.
     *
     * select * from tbl_incomes where profile_id = ?1 order by date desc limit ?2
     *
     * The @EntityGraph annotation is used to specify that the category and profile associations,
     * should be fetched eagerly when executing this query. This helps to avoid the N+1 select problem
     * and improves performance by reducing the number of queries executed against the database.
     */
    @EntityGraph(attributePaths = {"category", "profile"})
    List<IncomeEntity> findByProfileIdOrderByDateDesc(Long profileId, Pageable pageable);

    /*
     * Finds an income by id and profile id.
     *
     * select * from tbl_incomes where id = ?1 and profile_id = ?2
     *
     * The @EntityGraph annotation is used to specify that the category and profile associations,
     * should be fetched eagerly when executing this query. This helps to avoid the N+1 select problem
     * and improves performance by reducing the number of queries executed against the database.
     */
    @EntityGraph(attributePaths = {"category", "profile"})
    Optional<IncomeEntity> findIncomeWithCategoryAndProfileById(Long id);

    /*
     * Finds the total incomes by profile id.
     *
     * select sum(amount) from tbl_incomes where profile_id = ?1
     */
    @Query("SELECT SUM(i.amount) FROM IncomeEntity i WHERE i.profile.id = :profileId")
    BigDecimal findTotalIncomesByProfileId(@Param("profileId") Long profileId);

    /*
     * Finds all incomes by profile id and date.
     *
     * select * from tbl_incomes where profile_id = ?1 and date = ?2
     *
     * The @EntityGraph annotation is used to specify that the category and profile associations,
     * should be fetched eagerly when executing this query. This helps to avoid the N+1 select problem
     * and improves performance by reducing the number of queries executed against the database.
     */
    @EntityGraph(attributePaths = {"category", "profile"})
    List<IncomeEntity> findByProfileIdAndDate(Long profileId, LocalDate date);
}