package isalem.dev.budget_buddy.repositories;

import isalem.dev.budget_buddy.entities.CategoryEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    /*
     * Finds all categories by profile id.
     *
     * select * from tbl_categories where profile_id = ?1
     *
     * This annotation is used to specify that when fetching CategoryEntity,
     * the associated ProfileEntity should also be fetched eagerly (in the same query) to avoid lazy loading issues.
     */
    @EntityGraph(attributePaths = "profile")
    List<CategoryEntity> findAllByProfileId(Long profile_id);

    /*
     * Finds all categories by profile id and type.
     *
     * select * from tbl_categories where profile_id = ?1 and type = ?2
     *
     * This annotation is used to specify that when fetching CategoryEntity,
     * the associated ProfileEntity should also be fetched eagerly (in the same query) to avoid lazy loading issues.
     */
    @EntityGraph(attributePaths = "profile")
    List<CategoryEntity> findAllByProfileIdAndType(Long profile_id, String type);

    /*
     * Finds a category by id and profile id.
     *
     * select * from tbl_categories where id = ?1 and profile_id = ?2
     *
     * This annotation is used to specify that when fetching CategoryEntity,
     * the associated ProfileEntity should also be fetched eagerly (in the same query) to avoid lazy loading issues.
     */
    @EntityGraph(attributePaths = "profile")
    Optional<CategoryEntity> findByIdAndProfileId(Long id, Long profile_id);

    /*
     * Checks if a category exists by name and profile id.
     * select count(*) > 0 from tbl_categories where name = ?1 and profile_id = ?2
     */
    Boolean existsByNameAndProfileId(String name, Long profile_id);
}