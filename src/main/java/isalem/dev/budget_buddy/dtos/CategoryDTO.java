package isalem.dev.budget_buddy.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
 * This DTO is used for both creating and retrieving categories.
 * It contains only the necessary fields for both operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long id; // from CategoryEntity

    private Long profileId; // from CategoryEntity -> ProfileEntity

    private String name; // from CategoryEntity

    private String icon; // from CategoryEntity

    private String type; // from CategoryEntity

    private LocalDateTime createdDate; // from CategoryEntity

    private LocalDateTime updatedDate; // from CategoryEntity
}