package com.michael1099.rest_rpg.skill;

import com.michael1099.rest_rpg.auth.auth.AuthenticationFacade;
import com.michael1099.rest_rpg.character.CharacterRepository;
import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.exceptions.SkillAlreadyExistsException;
import com.michael1099.rest_rpg.helpers.SearchHelper;
import com.michael1099.rest_rpg.skill.model.Skill;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.CharacterSkillBasics;
import org.openapitools.model.SkillBasicPage;
import org.openapitools.model.SkillCreateRequest;
import org.openapitools.model.SkillDetails;
import org.openapitools.model.SkillLite;
import org.openapitools.model.SkillLites;
import org.openapitools.model.SkillSearchRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class SkillService {

    private final SkillRepository skillRepository;
    private final CharacterRepository characterRepository;
    private final AuthenticationFacade authenticationFacade;
    private final SkillMapper skillMapper;

    @Transactional
    public SkillLite createSkill(@NotNull SkillCreateRequest skillCreateRequest) {
        var dto = skillMapper.toDto(skillCreateRequest);
        checkIfSkillExists(dto.getName());
        var skill = Skill.of(dto);
        return skillMapper.toLite(skillRepository.save(skill));
    }

    @Transactional
    public SkillBasicPage findSkills(@NotNull SkillSearchRequest request) {
        var pageable = SearchHelper.getPageable(request.getPagination());
        return skillMapper.toPage(skillRepository.findSkills(request, pageable));
    }

    public SkillDetails getSkill(long skillId) {
        return skillMapper.toDetails(skillRepository.get(skillId));
    }

    public SkillLites getSkills() {
        return skillMapper.toLites(skillRepository.findByDeletedFalse());
    }

    public CharacterSkillBasics getCharacterSkills(long characterId) {
        var character = characterRepository.getWithEntityGraph(characterId, Character.CHARACTER_SKILLS);
        authenticationFacade.checkIfCharacterBelongsToUser(character);

        return skillMapper.toCharacterSkillBasics(character);
    }

    @Transactional
    public SkillLite learnSkill(long skillId, long characterId) {
        var character = characterRepository.getWithEntityGraph(characterId, Character.CHARACTER_SKILLS);
        authenticationFacade.checkIfCharacterBelongsToUser(character);
        character.getOccupation().throwIfCharacterIsOccupied();
        var skill = skillRepository.get(skillId);
        character.learnNewSkill(skill);
        characterRepository.save(character);
        return skillMapper.toLite(skill);
    }

    private void checkIfSkillExists(@NotBlank String skillName) {
        if (skillRepository.existsByNameIgnoreCase(skillName)) {
            throw new SkillAlreadyExistsException();
        }
    }
}
