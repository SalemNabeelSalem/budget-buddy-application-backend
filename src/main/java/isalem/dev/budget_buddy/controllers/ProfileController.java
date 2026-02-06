package isalem.dev.budget_buddy.controllers;

import isalem.dev.budget_buddy.dtos.ProfileDTO;
import isalem.dev.budget_buddy.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> registerNewProfile(@RequestBody ProfileDTO profileDTO) {
        ProfileDTO newRegisteredProfile = profileService.registerNewProfile(profileDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(newRegisteredProfile);
    }
}