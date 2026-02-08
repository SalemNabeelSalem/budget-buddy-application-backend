package isalem.dev.budget_buddy.controllers;

import isalem.dev.budget_buddy.dtos.ProfileDTO;
import isalem.dev.budget_buddy.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/profiles")
@RequiredArgsConstructor
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
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Profile activated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token not found or already used.");
        }
    }
}