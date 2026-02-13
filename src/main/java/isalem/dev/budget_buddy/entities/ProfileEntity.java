package isalem.dev.budget_buddy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity // Marks this class as a JPA entity
@Table(name = "tbl_profiles") // Specifies the table name in the database
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode methods
@Builder // Lombok annotation to implement the builder pattern for this class
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all fields as parameters
public class ProfileEntity {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing primary key
    private Long id;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String password;

    private String profileImageUrl;

    @CreationTimestamp // Automatically sets the timestamp when the entity is created
    @Column(updatable = false) // Prevents the createdAt field from being updated after it's set
    private LocalDateTime createdAt;

    @UpdateTimestamp // Automatically updates the timestamp whenever the entity is updated
    private LocalDateTime updatedAt;

    private Boolean isActive;

    private String activationToken;

    @PrePersist // This method will be called before the entity is persisted (saved) to the database
    public void prePersist() {
        if (this.isActive == null) {
            this.isActive = false;
        }
    }
}