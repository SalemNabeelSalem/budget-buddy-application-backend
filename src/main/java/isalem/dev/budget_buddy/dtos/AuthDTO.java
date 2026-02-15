package isalem.dev.budget_buddy.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/*
 * Data Transfer Object (DTO) for authentication requests and responses.
 * This class encapsulates the necessary information for user authentication, including email, password, and token.
 **/
public class AuthDTO {

    private String email; // from ProfileEntity

    private String password; // from ProfileEntity

    private String token; // JWT token generated upon successful authentication
}