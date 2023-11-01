package com.michael1099.rest_rpg.statistics;

import com.michael1099.rest_rpg.auth.auth.IAuthenticationFacade;
import com.michael1099.rest_rpg.auth.user.UserRepository;
import com.michael1099.rest_rpg.exceptions.CharacterNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.StatisticsDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final UserRepository userRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final StatisticsMapper statisticsMapper;

    @Transactional
    public StatisticsDetails getStatistics(long characterId) {
        var username = authenticationFacade.getAuthentication().getName();
        if (userRepository.getByUsername(username).getCharacters().stream().noneMatch(character -> character.getId() == characterId)) {
            throw new CharacterNotFoundException();
        }

        return statisticsMapper.toStatisticsDetails(statisticsRepository.getStatisticsByCharacterId(characterId));
    }
}
