package isalem.dev.budget_buddy.repositories;

import isalem.dev.budget_buddy.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

    /*
     * Finds all categories by profile id.
     * select * from tbl_categories where profile_id = ?
     */
    List<CategoryEntity> findAllByProfileId(Long profile_id);

    /*
     * Finds all categories by profile id and type.
     * select * from tbl_categories where profile_id = ? and type = ?
     */
    List<CategoryEntity> findAllByProfileIdAndType(Long profile_id, String type);

    /*
     * Finds a category by id and profile id.
     * select * from tbl_categories where id = ? and profile_id = ?
     */
    Optional<CategoryEntity> findByIdAndProfileId(Long id, Long profile_id);

    /*
     * Checks if a category exists by name and profile id.
     * select count(*) > 0 from tbl_categories where name = ? and profile_id = ?
     */
    Boolean existsByNameAndProfileId(String name, Long profile_id);
}