package com.michael1099.rest_rpg.skill;

import com.michael1099.rest_rpg.auth.auth.AuthenticationFacade;
import com.michael1099.rest_rpg.character.CharacterRepository;
import com.michael1099.rest_rpg.character.mediator.LearnSkillMediator;
import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.config.RepositoryDecorator;
import com.michael1099.rest_rpg.exceptions.NotEnoughGoldException;
import com.michael1099.rest_rpg.exceptions.NotEnoughSkillPointsException;
import com.michael1099.rest_rpg.exceptions.SkillAlreadyExistsException;
import com.michael1099.rest_rpg.exceptions.SkillCharacterClassMismatchException;
import com.michael1099.rest_rpg.helpers.SearchHelper;
import com.michael1099.rest_rpg.skill.model.Skill;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Validated
public class SkillService {

    private final SkillRepository skillRepository;
    private final RepositoryDecorator<Skill, Long> decoratedRepository;
    private final CharacterRepository characterRepository;
    private final AuthenticationFacade authenticationFacade;
    private final SkillMapper skillMapper;
    private final SkillCache skillCache;

    public SkillService(SkillRepository skillRepository, CharacterRepository characterRepository, AuthenticationFacade authenticationFacade, SkillMapper skillMapper, SkillCache skillCache) {
        this.skillRepository = skillRepository;
        this.decoratedRepository = new RepositoryDecorator<>(skillRepository);
        this.characterRepository = characterRepository;
        this.authenticationFacade = authenticationFacade;
        this.skillMapper = skillMapper;
        this.skillCache = skillCache;
    }

    @Transactional
    public SkillLite createSkill(@NotNull SkillCreateRequest skillCreateRequest) {
        var dto = skillMapper.toDto(skillCreateRequest);
        checkIfSkillExists(dto.getName());
        var skill = Skill.of(dto);
        return skillMapper.toLite(decoratedRepository.save(skill));
    }

    @Transactional
    public SkillBasicPage findSkills(@NotNull SkillSearchRequest request) {
        var pageable = SearchHelper.getPageable(request.getPagination());
        return skillMapper.toPage(skillRepository.findSkills(request, pageable));
    }

    @Transactional
    public SkillDetails getSkill(long skillId) {
        SkillDetails details;
        if (skillCache.containsEntity(skillId)) {
            details = skillMapper.toDetails(skillCache.getEntity(skillId));
        } else {
            var entity = skillRepository.get(skillId);
            skillCache.putEntity(skillId, entity);
            details = skillMapper.toDetails(entity);
        }
        return details;
    }

    @Transactional
    public SkillLites getSkills() {
        return skillMapper.toLites(skillRepository.findByDeletedFalse());
    }

    @Transactional
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
        validateSkillLearning(character, skill);
        var mediator = new LearnSkillMediator(character.getEquipment(), character, character.getStatistics(), character.getSkills());
        mediator.learnNewSkill(skill);
        characterRepository.save(character);
        return skillMapper.toLite(skill);
    }

    private void checkIfSkillExists(@NotBlank String skillName) {
        if (skillRepository.existsByNameIgnoreCase(skillName)) {
            throw new SkillAlreadyExistsException();
        }
    }

    private void validateSkillLearning(@NotNull Character character, @NotNull Skill skill) {
        if (!skill.getCharacterClass().equals(character.getCharacterClass())) {
            throw new SkillCharacterClassMismatchException();
        }
        if (character.getEquipment().getGold() < skill.getSkillTraining().getGoldCost()) {
            throw new NotEnoughGoldException();
        }
        if (character.getStatistics().getFreeStatisticPoints() < skill.getSkillTraining().getStatisticPointsCost()) {
            throw new NotEnoughSkillPointsException();
        }
    }
}
