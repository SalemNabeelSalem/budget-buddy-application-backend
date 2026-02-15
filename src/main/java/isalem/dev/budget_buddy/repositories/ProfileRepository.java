package isalem.dev.budget_buddy.repositories;

import isalem.dev.budget_buddy.entities.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository <ProfileEntity, Long> {

    /*
     * Finds a profile by email.
     * select * from tbl_profiles where email = ?1
     */
    Optional<ProfileEntity> findByEmail(String email);

    /*
     * Finds a profile by activation token.
     * select * from tbl_profiles where activation_token = ?2
     */
    Optional<ProfileEntity> findByActivationToken(String activationToken);
}