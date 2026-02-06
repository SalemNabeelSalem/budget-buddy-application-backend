package isalem.dev.budget_buddy.repositories;

import isalem.dev.budget_buddy.entities.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository <ProfileEntity, Long> {

    /**
     * Finds a profile by email.
     * select * from tbl_profiles where email = ?
     * */
    Optional<ProfileEntity> findByEmail(String email);
}