package isalem.dev.budget_buddy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tbl_categories")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CategoryEntity {

    @Id // Primary key
    @ToString.Include // Include the id field in the toString() output
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing primary key
    private Long id;

    private String name;

    private String type;

    private String icon;

    @CreationTimestamp // Automatically sets the timestamp when the entity is created
    @Column(updatable = false) // Prevents the createdAt field from being updated after it's set
    private LocalDateTime createdAt;

    @UpdateTimestamp // Automatically updates the timestamp whenever the entity is updated
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY) // Many categories can belong to one profile, and the profile data will be loaded lazily (only when accessed)
    @JoinColumn(name = "profile_id", nullable = false) // Specifies the foreign key column name and makes it non-nullable
    private ProfileEntity profile;
}