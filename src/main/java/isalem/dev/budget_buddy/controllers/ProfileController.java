package isalem.dev.budget_buddy.controllers;

import isalem.dev.budget_buddy.dtos.AuthDTO;
import isalem.dev.budget_buddy.dtos.ProfileDTO;
import isalem.dev.budget_buddy.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> registerNewProfile(@RequestBody ProfileDTO profileDTO) {
        ProfileDTO newRegisteredProfile = profileService.registerNewProfile(profileDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(newRegisteredProfile);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(@RequestParam String token) {
        boolean activationResult = profileService.activateProfile(token);

        if (activationResult) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("profile activated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("activation token not found or already used.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthDTO authDto) {
        try {
            if (!profileService.isProfileActivated(authDto.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        Map.of(
                                "message",
                                "profile is not activated. please check your email for the activation link."
                        )
                );
            } else {
                Map<String, Object> loginResponse = profileService.authenticateAndGenerateToken(authDto);

                return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}