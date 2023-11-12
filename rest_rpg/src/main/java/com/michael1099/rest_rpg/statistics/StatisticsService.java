package com.michael1099.rest_rpg.statistics;

import com.michael1099.rest_rpg.auth.auth.IAuthenticationFacade;
import com.michael1099.rest_rpg.character.CharacterRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.StatisticsDetails;
import org.openapitools.model.StatisticsUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final CharacterRepository characterRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final StatisticsMapper statisticsMapper;

    @Transactional
    public StatisticsDetails getStatistics(long characterId) {
        var character = characterRepository.getCharacterById(characterId);
        authenticationFacade.checkIfCharacterBelongsToUser(character);

        return statisticsMapper.toStatisticsDetails(statisticsRepository.getStatisticsByCharacterId(characterId));
    }

    @Transactional
    public StatisticsDetails trainCharacter(long characterId, @NotNull StatisticsUpdateRequest request) {
        var character = characterRepository.getCharacterById(characterId);
        authenticationFacade.checkIfCharacterBelongsToUser(character);
        character.getOccupation().throwIfCharacterIsOccupied();

        var dto = statisticsMapper.toStatisticsUpdateRequestDto(request);
        var statistics = statisticsRepository.getStatisticsByCharacterId(characterId);
        statistics.train(dto);

        return statisticsMapper.toStatisticsDetails(statisticsRepository.save(statistics));
    }
}
