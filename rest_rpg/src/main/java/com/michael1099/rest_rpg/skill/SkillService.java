package com.michael1099.rest_rpg.skill;

import com.michael1099.rest_rpg.exceptions.SkillAlreadyExistsException;
import com.michael1099.rest_rpg.skill.model.Skill;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.SkillBasicPage;
import org.openapitools.model.SkillCreateRequest;
import org.openapitools.model.SkillDetails;
import org.openapitools.model.SkillLite;
import org.openapitools.model.SkillLites;
import org.openapitools.model.SkillSearchRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    public SkillLite createSkill(@NotNull SkillCreateRequest skillCreateRequest) {
        var dto = skillMapper.toDto(skillCreateRequest);
        checkIfSkillExists(dto.getName());
        var skill = Skill.of(dto);
        return skillMapper.toLite(skillRepository.save(skill));
    }

    public SkillBasicPage findSkills(@NotNull SkillSearchRequest request) {
        var pageableSort = Sort.by(request.getPagination().getSort());
        pageableSort = request.getPagination().getSortOrder().equalsIgnoreCase("asc") ? pageableSort.ascending() : pageableSort.descending();
        pageableSort = pageableSort.and(Sort.by("id"));
        var pageable = PageRequest.of(request.getPagination().getPageNumber(), request.getPagination().getElements(), pageableSort);
        return skillMapper.toPage(skillRepository.findSkills(request, pageable));
    }

    public SkillDetails getSkill(long skillId) {
        return skillMapper.toDetails(skillRepository.get(skillId));
    }

    public SkillLites getSkills() {
        return skillMapper.toLites(skillRepository.findByDeletedFalse());
    }

    private void checkIfSkillExists(@NotBlank String skillName) {
        if (skillRepository.existsByNameIgnoreCase(skillName)) {
            throw new SkillAlreadyExistsException();
        }
    }
}
