package com.michael1099.rest_rpg.character;

import com.michael1099.rest_rpg.auth.user.UserRepository;
import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.exceptions.CharacterAlreadyExistsException;
import com.michael1099.rest_rpg.exceptions.NotEnoughSkillPointsException;
import com.michael1099.rest_rpg.exceptions.UserNotFoundException;
import com.michael1099.rest_rpg.statistics.dto.StatisticsUpdateRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.CharacterCreateRequest;
import org.openapitools.model.CharacterLite;
import org.springframework.stereotype.Service;

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
