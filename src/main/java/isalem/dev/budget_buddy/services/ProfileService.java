package isalem.dev.budget_buddy.services;

import isalem.dev.budget_buddy.dtos.AuthDTO;
import isalem.dev.budget_buddy.dtos.ProfileDTO;
import isalem.dev.budget_buddy.entities.ProfileEntity;
import isalem.dev.budget_buddy.repositories.ProfileRepository;
import isalem.dev.budget_buddy.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public ProfileDTO registerNewProfile(ProfileDTO profileDTO) {

        ProfileEntity newProfileEntity = toProfileEntity(profileDTO);

        newProfileEntity.setActivationToken(UUID.randomUUID().toString());

        newProfileEntity = profileRepository.save(newProfileEntity);

        /*
         * Send activation email to the user with the activation token.
         **/
        String activationLink = "http://localhost:8080/api/v1.0/profile/activate?token=" + newProfileEntity.getActivationToken();

        String emailSubject = "Activate Your Budget Buddy Profile";

        // String emailBody = "Welcome to Budget Buddy! Please activate your profile by clicking the following link: " + activationLink;

        String htmlEmailTemplate = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Activate your Budget Buddy profile</title>
                <style>
                    body { font-family: Arial, sans-serif; background-color: #f4f6f8; margin: 0; padding: 0; }
                    .container { max-width: 600px; margin: 10px auto; background-color: #ffffff; border-radius: 8px;
                                 box-shadow: 0 4px 12px rgba(0,0,0,0.06); }
                    .header { text-align: center; border-bottom: 1px solid #e5e7eb; }
                    .header h1 { font-size: 24px; color: #111827; }
                    .content { font-size: 15px; color: #374151; line-height: 1.6; }
                    .btn-wrapper { margin: 10px 0; text-align: center; }
                    .btn { display: inline-block; padding: 10px 10px; background-color: #2563eb; color: #ffffff !important;
                           text-decoration: none; border-radius: 999px; font-weight: 600; font-size: 14px; }
                    .btn:hover { background-color: #1d4ed8; }
                    .link-fallback { font-size: 13px; color: #6b7280; margin-top: 10px; word-break: break-all; }
                    .footer { margin-top: 10px; font-size: 12px; color: #9ca3af; text-align: center; }
                </style>
            </head>
            <body>
            <div class="container">
                <div class="header">
                    <h1>Welcome to Budget Buddy 🎉</h1>
                </div>
                <div class="content">
                    <p>Hi,</p>
                    <p>
                        Thanks for signing up for <strong>Budget Buddy</strong>!
                        To activate your profile and start tracking your finances, please confirm your email address.
                    </p>
                    <div class="btn-wrapper">
                        <a href="%s" class="btn">Activate my profile</a>
                    </div>
                    <p>
                        If you didn’t create this profile, you can safely ignore this email.
                    </p>
                </div>
                <div class="footer">
                    &copy; %s Budget Buddy. All rights reserved.
                </div>
            </div>
            </body>
            </html>
            """;

        String emailBody = String.format(htmlEmailTemplate, activationLink, java.time.Year.now().getValue());

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
}