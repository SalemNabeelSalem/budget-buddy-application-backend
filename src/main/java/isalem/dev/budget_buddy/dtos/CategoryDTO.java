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

    private Long id;

    private Long profileId;

    private String name;

    private String icon;

    private String type;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}