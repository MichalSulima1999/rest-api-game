package com.michael1099.rest_rpg.report;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.character_skill.CharacterSkill;
import com.michael1099.rest_rpg.skill.model.Skill;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.openapitools.model.ReportResponse;

import java.util.LinkedHashMap;

import static com.michael1099.rest_rpg.report.YamlReportGenerator.convertToYaml;

@RequiredArgsConstructor
public class CharacterReportManagerImplementation implements ReportManager {

    private final Character character;

    @Override
    public ReportResponse generateDefaultReport() {
        var name = "Character " + character.getName() + " report";
        var skills = character.getSkills().stream().map(CharacterSkill::getSkill).map(Skill::getName).toList();
        var content = "Race: " + character.getRace() +
                "sex: " + character.getSex() +
                "class: " + character.getCharacterClass().toString() +
                "skills: " + skills;
        return new ReportResponse(name, content);
    }

    @Override
    public ReportResponse generateJsonReport() {
        var name = "Character " + character.getName() + " report";
        var skills = character.getSkills().stream().map(CharacterSkill::getSkill).map(Skill::getName).toList();
        var content = new JSONObject()
                .put("race", character.getRace())
                .put("sex", character.getSex())
                .put("class", character.getCharacterClass().toString())
                .put("skills", skills);
        return new ReportResponse(name, content.toString());
    }

    @Override
    public ReportResponse generateYamlReport() {
        var name = "Character " + character.getName() + " report";
        var skills = character.getSkills().stream().map(CharacterSkill::getSkill).map(Skill::getName).toList();
        var content = new LinkedHashMap<>();
        content.put("race", character.getRace());
        content.put("sex", character.getSex());
        content.put("class", character.getCharacterClass().toString());
        content.put("skills", skills);
        return new ReportResponse(name, convertToYaml(content));
    }
}
