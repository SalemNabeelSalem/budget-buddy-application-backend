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

    private Long id;

    private String fullName;

    private String email;

    private String password;

    private String profileImageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}