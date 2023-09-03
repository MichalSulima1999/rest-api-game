package com.michael1099.rest_rpg.statistics;

import com.michael1099.rest_rpg.auth.auth.IAuthenticationFacade;
import com.michael1099.rest_rpg.auth.user.UserRepository;
import com.michael1099.rest_rpg.exceptions.CharacterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.StatisticsDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final UserRepository userRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final StatisticsMapper statisticsMapper;

    public StatisticsDetails getStatistics(long characterId) {
        var username = authenticationFacade.getAuthentication().getName();
        if (userRepository.getByUsername(username).getCharacters().stream().noneMatch(character -> character.getId() == characterId)) {
            throw new CharacterNotFoundException();
        }

        return statisticsMapper.toStatisticsDetails(statisticsRepository.getStatisticsByCharacterId(characterId));
    }
}
