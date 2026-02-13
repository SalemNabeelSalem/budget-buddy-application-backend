package isalem.dev.budget_buddy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {

    @Id // Primary key
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

    @JoinColumn(name = "profile_id", nullable = false) // Specifies the foreign key column name and makes it non-nullable
    @ManyToOne(fetch = FetchType.LAZY) // Many categories can belong to one profile, and the profile data will be loaded lazily (only when accessed)
    private ProfileEntity profile;
}