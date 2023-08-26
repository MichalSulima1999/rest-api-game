package com.michael1099.rest_rpg.character;

import com.michael1099.rest_rpg.auth.user.UserRepository;
import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.character.model.CharacterArtwork;
import com.michael1099.rest_rpg.exceptions.CharacterAlreadyExistsException;
import com.michael1099.rest_rpg.exceptions.GetImageException;
import com.michael1099.rest_rpg.exceptions.ImageDoesNotExistException;
import com.michael1099.rest_rpg.exceptions.NotEnoughSkillPointsException;
import com.michael1099.rest_rpg.exceptions.UserNotFoundException;
import com.michael1099.rest_rpg.statistics.dto.StatisticsUpdateRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.openapitools.model.CharacterCreateRequest;
import org.openapitools.model.CharacterLite;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final UserRepository userRepository;
    private final CharacterMapper characterMapper;

    public CharacterLite createCharacter(@NotNull CharacterCreateRequest request, @NotBlank String username) {
        var dto = characterMapper.toDto(request);
        assertCharacterDoesNotExist(dto.getName());
        var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        var character = Character.createCharacter(dto, user);
        assertCharacterStatisticsAreValid(dto.getStatistics(), character.getStatistics().getFreeStatisticPoints());
        character.getStatistics().addStatistics(dto.getStatistics());

        return characterMapper.toLite(characterRepository.save(character));
    }

    public ResponseEntity<Resource> getCharacterFullArtwork(@NotNull String characterArtwork) {
        return getCharacterArtwork(characterArtwork, "public/avatars/full/");
    }

    public ResponseEntity<Resource> getCharacterThumbnailArtwork(@NotNull String characterArtwork) {
        return getCharacterArtwork(characterArtwork, "public/avatars/thumbnail/");
    }

    public List<String> getCharacterArtworkEnum() {
        return Arrays.stream(CharacterArtwork.values()).map(Objects::toString).collect(Collectors.toList());
    }

    private void assertCharacterDoesNotExist(@NotNull String name) {
        if (characterRepository.existsByNameIgnoreCase(name)) {
            throw new CharacterAlreadyExistsException();
        }
    }

    private void assertCharacterStatisticsAreValid(@NotNull StatisticsUpdateRequestDto dto, int freePoints) {
        var sum = dto.getConstitution() + dto.getDexterity() + dto.getStrength() + dto.getIntelligence();
        if (sum > freePoints) {
            throw new NotEnoughSkillPointsException();
        }
    }

    private ResponseEntity<Resource> getCharacterArtwork(@NotNull String characterArtwork, @NotNull String artworksPath) {
        if (!EnumUtils.isValidEnum(CharacterArtwork.class, characterArtwork)) {
            throw new ImageDoesNotExistException();
        }
        String inputFile = artworksPath + CharacterArtwork.valueOf(characterArtwork).getArtworkName();
        Path path = new File(inputFile).toPath();
        FileSystemResource resource = new FileSystemResource(path);
        MediaType mediaType = null;
        try {
            mediaType = MediaType.parseMediaType(Files.probeContentType(path));
        } catch (IOException e) {
            throw new GetImageException();
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }
}
