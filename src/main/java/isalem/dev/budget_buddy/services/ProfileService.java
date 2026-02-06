package isalem.dev.budget_buddy.services;

import isalem.dev.budget_buddy.dtos.ProfileDTO;
import isalem.dev.budget_buddy.entities.ProfileEntity;
import isalem.dev.budget_buddy.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Marks this class as a Spring service component, making it eligible for component scanning and dependency injection.
 * */
@Service //
@RequiredArgsConstructor // Lombok annotation to generate a constructor with required arguments (final fields)
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileDTO registerNewProfile(ProfileDTO profileDTO) {
        ProfileEntity newProfileEntity = toProfileEntity(profileDTO);

        newProfileEntity.setActivationToken(UUID.randomUUID().toString());

        return toProfileDTO(profileRepository.save(newProfileEntity));
    }

    public ProfileEntity toProfileEntity(ProfileDTO profileDTO) {
        return ProfileEntity.builder()
                .fullName(profileDTO.getFullName())
                .email(profileDTO.getEmail())
                .password(profileDTO.getPassword())
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
}