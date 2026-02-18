package isalem.dev.budget_buddy.repositories;

import isalem.dev.budget_buddy.entities.IncomeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<IncomeEntity, Long> {

    /*
     * Finds all incomes by profile id ordered by date descending.
     * select * from tbl_incomes where profile_id = ?1 order by date desc
     */
    List<IncomeEntity> findByProfileIdOrderByDateDesc(Long profileId);

    /*
     * Finds all incomes by profile id and date between start date and end date.
     * select * from tbl_incomes where profile_id = ?1 and date between ?2 and ?3 order by date desc
     */
    List<IncomeEntity> findByProfileIdAndDateBetweenOrderByDateDesc(Long profileId, LocalDate startDate, LocalDate endDate);

    /*
     * Finds all incomes by profile id and date between start date and end date containing name ignoring case.
     * select * from tbl_incomes where profile_id = ?1 and date between ?2 and ?3 and name like %?4%
     */
    List<IncomeEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            String name,
            Sort sort
    );

    /*
     * Finds the top 5 incomes by profile id ordered by date descending.
     * select * from tbl_incomes where profile_id = ?1 order by date desc limit 5
     */
    List<IncomeEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

    /*
     * Finds the total incomes by profile id.
     * select sum(amount) from tbl_incomes where profile_id = ?1
     */
    @Query("SELECT SUM(i.amount) FROM IncomeEntity i WHERE i.profile.id = :profileId")
    BigDecimal findTotalIncomesByProfileId(@Param("profileId") Long profileId);
}