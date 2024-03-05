package com.michael1099.rest_rpg.character;

import com.michael1099.rest_rpg.auth.auth.IAuthenticationFacade;
import com.michael1099.rest_rpg.auth.user.UserRepository;
import com.michael1099.rest_rpg.character.artwork.CharacterCompositeArtwork;
import com.michael1099.rest_rpg.character.artwork.CharacterFullArtwork;
import com.michael1099.rest_rpg.character.artwork.CharacterThumbnailArtwork;
import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.character.model.CharacterArtwork;
import com.michael1099.rest_rpg.character.model.dto.CharacterCreateRequestDto;
import com.michael1099.rest_rpg.exceptions.CharacterAlreadyExistsException;
import com.michael1099.rest_rpg.exceptions.CharacterNotFoundException;
import com.michael1099.rest_rpg.exceptions.EnumValueNotFoundException;
import com.michael1099.rest_rpg.exceptions.GetImageException;
import com.michael1099.rest_rpg.exceptions.ImageDoesNotExistException;
import com.michael1099.rest_rpg.exceptions.NotEnoughSkillPointsException;
import com.michael1099.rest_rpg.statistics.dto.StatisticsUpdateRequestDto;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.openapitools.model.CharacterBasics;
import org.openapitools.model.CharacterCreateRequest;
import org.openapitools.model.CharacterDetails;
import org.openapitools.model.CharacterLite;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
@Validated
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final UserRepository userRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final CharacterMapper characterMapper;

    @Transactional
    public CharacterLite createCharacter(@NotNull CharacterCreateRequest request, @NotBlank String username) {
        CharacterCreateRequestDto dto;
        try {
            dto = characterMapper.toDto(request);
        } catch (IllegalArgumentException exception) {
            throw new EnumValueNotFoundException();
        }
        assertCharacterDoesNotExist(dto.getName());
        var user = userRepository.getByUsername(username);
        var character = Character.createCharacter(dto, user);
        assertCharacterStatisticsAreValid(dto.getStatistics(), character.getStatistics().getFreeStatisticPoints());
        character.getStatistics().addStatistics(dto.getStatistics(), dto.getRace());

        return characterMapper.toLite(characterRepository.save(character));
    }

    @Transactional
    public ResponseEntity<Resource> getCharacterFullArtwork(@NotNull String characterArtwork) {
        var artwork = new CharacterFullArtwork(this);
        return new CharacterCompositeArtwork(artwork).getArtwork(characterArtwork);
    }

    @Transactional
    public ResponseEntity<Resource> getCharacterThumbnailArtwork(@NotNull String characterArtwork) {
        var artwork = new CharacterThumbnailArtwork(this);
        return new CharacterCompositeArtwork(artwork).getArtwork(characterArtwork);
    }

    @Transactional
    public List<String> getCharacterArtworkEnum() {
        return Arrays.stream(CharacterArtwork.values()).map(Objects::toString).collect(Collectors.toList());
    }

    @Transactional
    public CharacterBasics getUserCharacters() {
        var username = authenticationFacade.getAuthentication().getName();
        return characterMapper.toBasics(characterRepository.findByUser_Username(username), 1);
    }

    @Transactional
    public CharacterDetails getUserCharacter(long characterId) {
        var username = authenticationFacade.getAuthentication().getName();
        var character = characterRepository.getCharacterById(characterId);
        if (!Objects.equals(character.getUser().getId(), userRepository.getByUsername(username).getId())) {
            throw new CharacterNotFoundException();
        }

        return characterMapper.toDetails(character);
    }

    @Transactional
    public ResponseEntity<Resource> getCharacterArtwork(@NotNull String characterArtwork, @NotNull String artworksPath) {
        if (!EnumUtils.isValidEnum(CharacterArtwork.class, characterArtwork)) {
            throw new ImageDoesNotExistException();
        }
        String inputFile = artworksPath + CharacterArtwork.valueOf(characterArtwork).getArtworkName();
        Path path = new File(inputFile).toPath();
        FileSystemResource resource = new FileSystemResource(path);
        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(Files.probeContentType(path));
        } catch (IOException e) {
            throw new GetImageException();
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
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
}
