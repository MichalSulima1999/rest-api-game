package com.michael1099.rest_rpg.work;

import com.michael1099.rest_rpg.auth.auth.AuthenticationFacade;
import com.michael1099.rest_rpg.character.CharacterRepository;
import com.michael1099.rest_rpg.exceptions.CharacterStillWorkingException;
import com.michael1099.rest_rpg.exceptions.WorkAlreadyExistsException;
import com.michael1099.rest_rpg.exceptions.WorkNotFoundException;
import com.michael1099.rest_rpg.helpers.SearchHelper;
import com.michael1099.rest_rpg.work.model.Work;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.WorkCreateRequest;
import org.openapitools.model.WorkLite;
import org.openapitools.model.WorkLitePage;
import org.openapitools.model.WorkSearchRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class WorkService {

    private final WorkRepository workRepository;
    private final CharacterRepository characterRepository;
    private final AuthenticationFacade authenticationFacade;
    private final WorkMapper workMapper;

    @Transactional
    public WorkLite createWork(@NotNull WorkCreateRequest request) {
        var dto = workMapper.toDto(request);
        checkIfWorkExists(dto.getName());
        var work = workRepository.save(Work.of(dto));
        return workMapper.toLite(work);
    }

    @Transactional
    public WorkLitePage findWorks(@NotNull WorkSearchRequest request) {
        var pageable = SearchHelper.getPageable(request.getPagination());
        return workMapper.toPage(workRepository.findWorks(request, pageable));
    }

    @Transactional
    public void startWork(long workId, long characterId) {
        var character = characterRepository.getCharacterById(characterId);
        authenticationFacade.checkIfCharacterBelongsToUser(character);
        character.getOccupation().throwIfCharacterIsOccupied();

        var work = workRepository.getWorkById(workId);
        character.getOccupation().getState().startWork(work, character.getOccupation());
        characterRepository.save(character);
    }

    @Transactional
    public void endWork(long characterId) {
        var character = characterRepository.getCharacterById(characterId);
        authenticationFacade.checkIfCharacterBelongsToUser(character);
        if (Optional.ofNullable(character.getOccupation().getFinishTime()).orElseThrow(WorkNotFoundException::new).isAfter(LocalDateTime.now())) {
            throw new CharacterStillWorkingException();
        }
        var work = Optional.ofNullable(character.getOccupation().getWork())
                .orElseThrow(WorkNotFoundException::new);

        character.getOccupation().getState().endWork(work, character.getOccupation());
        characterRepository.save(character);
    }

    private void checkIfWorkExists(@NotBlank String workName) {
        if (workRepository.existsByNameIgnoreCase(workName)) {
            throw new WorkAlreadyExistsException();
        }
    }
}
