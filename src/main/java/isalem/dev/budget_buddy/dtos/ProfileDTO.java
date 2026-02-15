package isalem.dev.budget_buddy.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
 * This DTO is used for both registration and profile retrieval.
 * It contains all the necessary fields for both operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

    private Long id; // from ProfileEntity

    private String fullName; // from ProfileEntity

    private String email; // from ProfileEntity

    private String password; // from ProfileEntity - only used for registration, should be null when retrieving profile

    private String profileImageUrl; // from ProfileEntity

    private LocalDateTime createdAt; // from ProfileEntity

    private LocalDateTime updatedAt; // from ProfileEntity
}