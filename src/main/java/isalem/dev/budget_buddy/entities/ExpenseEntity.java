package isalem.dev.budget_buddy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_expenses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseEntity {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing primary key
    private Long id;

    private String name;

    private String icon;

    private LocalDate date;

    private BigDecimal amount;

    @CreationTimestamp // Automatically sets the timestamp when the entity is created
    @Column(updatable = false) // Prevents the createdAt field from being updated after it's set
    private LocalDateTime createdAt;

    @UpdateTimestamp // Automatically updates the timestamp whenever the entity is updated
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY) // Many expenses can belong to one category, and the category data will be loaded lazily (only when accessed)
    @JoinColumn(name = "category_id", nullable = false) // Specifies the foreign key column name and makes it non-nullable
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY) // Many expenses can belong to one profile, and the profile data will be loaded lazily (only when accessed)
    @JoinColumn(name = "profile_id", nullable = false) // Specifies the foreign key
    private ProfileEntity profile;

    @PrePersist // This method will be called before the entity is persisted (saved) to the database
    protected void prePersist() {
        if (this.date == null) {
            this.date = LocalDate.now();
        }
    }
}