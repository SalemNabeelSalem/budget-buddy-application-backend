package isalem.dev.budget_buddy.services;

import isalem.dev.budget_buddy.dtos.AuthDTO;
import isalem.dev.budget_buddy.dtos.ProfileDTO;
import isalem.dev.budget_buddy.entities.ProfileEntity;
import isalem.dev.budget_buddy.repositories.ProfileRepository;
import isalem.dev.budget_buddy.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
 * Marks this class as a Spring service component, making it eligible for component scanning and dependency injection.
 */
@Service //
@RequiredArgsConstructor // Lombok annotation to generate a constructor with required arguments (final fields)
public class ProfileService {

    private final ProfileRepository profileRepository;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;

    @Value("${budget-buddy.backend.base-url}")
    private String backendBaseUrl;

    public ProfileDTO registerNewProfile(ProfileDTO profileDTO) {

        ProfileEntity newProfileEntity = toProfileEntity(profileDTO);

        newProfileEntity.setActivationToken(UUID.randomUUID().toString());

        newProfileEntity = profileRepository.save(newProfileEntity);

        /*
         * Send activation email to the user with the activation token.
         **/
        String activationLink = backendBaseUrl + "/api/v1/profile/activate?token=" + newProfileEntity.getActivationToken();

        String emailSubject = "Activate Your Budget Buddy Profile";

        // String emailBody = "Welcome to Budget Buddy! Please activate your profile by clicking the following link: " + activationLink;

        String htmlEmailTemplate = """
            <!DOCTYPE html>
            <html>
                <head>
                </head>
                <body>
                    <h1>Welcome to Budget Buddy, %s!</h1>
                    <p style="font-size: 16px; color: #333;">Thank you for registering with Budget Buddy. To get started, please activate your profile by clicking the button below:</p>
                    <a href="%s" style="display: inline-block; padding: 20px; font-size: 16px; color: #fff; background-color: #007BFF; text-decoration: none; border-radius: 5px;">Activate Profile</a>
                    <p style="font-size: 14px; color: #555; margin: 30px 0px -20px 0;">If you did not create an account with us, please ignore this email.</p>
                    <p style="font-size: 14px; color: #555; margin-bottom: -20px;">Best regards,<br> The Budget Buddy Team❤️</p>
                    <p style="font-size: 12px; color: #888; margin-bottom: -20px;">This is an automatically generated email, please do not reply.</p>
                    <p style="font-size: 12px; color: #888;">&copy; %d Budget Buddy. All rights reserved.</p>
                </body>
            </html>
        """;

        String fullName = newProfileEntity.getFullName();

        String emailBody = String.format(htmlEmailTemplate, fullName, activationLink, java.time.Year.now().getValue());

        emailService.sendEmail(newProfileEntity.getEmail(), emailSubject, emailBody);

        return toProfileDTO(newProfileEntity);
    }

    public ProfileEntity toProfileEntity(ProfileDTO profileDTO) {
        return ProfileEntity.builder()
                .fullName(profileDTO.getFullName())
                .email(profileDTO.getEmail())
                .password(passwordEncoder.encode(profileDTO.getPassword()))
                .profileImageUrl(profileDTO.getProfileImageUrl())
                .createdAt(profileDTO.getCreatedAt())
                .updatedAt(profileDTO.getUpdatedAt())
                .build();
    }

    public ProfileDTO toProfileDTO(ProfileEntity profileEntity) {
        return ProfileDTO.builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
                .profileImageUrl(profileEntity.getProfileImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .updatedAt(profileEntity.getUpdatedAt())
                .build();
    }

    public boolean activateProfile(String activationToken) {
        return profileRepository.findByActivationToken(activationToken)
                .map(profile -> {
                    profile.setIsActive(true);
                    profile.setActivationToken(null); // Clear the token after activation
                    profileRepository.save(profile);
                    return true;
                })
                .orElse(false);
    }

    public boolean isProfileActivated(String email) {
        return profileRepository.findByEmail(email)
                .map(ProfileEntity::getIsActive)
                .orElse(false);
    }

    public ProfileEntity getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return profileRepository.findByEmail(authentication.getName())
                .orElseThrow(
                        () -> new UsernameNotFoundException("Profile not found for email: " + authentication.getName())
                );
    }

    public ProfileDTO getCurrentPublicProfile(String email) {
        ProfileEntity currentProfile;

        if (email == null || email.isEmpty()) {
            currentProfile = getCurrentProfile();
        } else {
            currentProfile = profileRepository.findByEmail(email)
                    .orElseThrow(
                            () -> new UsernameNotFoundException("Profile not found for email: " + email)
                    );
        }

        return toProfileDTO(currentProfile);
    }

    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authDto.getEmail(),
                            authDto.getPassword()
                    )
            );

            String jwtToken = jwtUtil.generateToken(authDto.getEmail());

            return Map.of(
                    "token", jwtToken,
                    "user", getCurrentPublicProfile(authDto.getEmail())
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password.");
        }
    }

    public ProfileEntity getProfileEntityById(Long profileId) {
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));
    }

    public List<ProfileDTO> getAllActivatedProfiles() {
        return profileRepository.findByIsActiveTrue()
                .stream()
                .map(this::toProfileDTO)
                .toList();
    }
}