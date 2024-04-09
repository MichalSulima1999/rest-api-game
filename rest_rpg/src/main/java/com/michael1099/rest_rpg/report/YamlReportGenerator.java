package com.michael1099.rest_rpg.report;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.character_skill.CharacterSkill;
import com.michael1099.rest_rpg.equipment.Equipment;
import com.michael1099.rest_rpg.skill.model.Skill;
import com.michael1099.rest_rpg.statistics.Statistics;
import org.openapitools.model.ReportResponse;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class YamlReportGenerator implements ReportVisitor {

    @Override
    public ReportResponse visit(Character character) {
        var name = "Character " + character.getName() + " report";
        var skills = character.getSkills().stream().map(CharacterSkill::getSkill).map(Skill::getName).toList();
        var content = new LinkedHashMap<>();
        content.put("race", character.getRace());
        content.put("sex", character.getSex());
        content.put("class", character.getCharacterClass().toString());
        content.put("skills", skills);
        return new ReportResponse(name, convertToYaml(content));
    }

    @Override
    public ReportResponse visit(Statistics statistics) {
        var name = "Statistics of character " + statistics.getCharacter().getName() + " report";
        var content = new LinkedHashMap<>();
        content.put("max_hp", statistics.getMaxHp());
        content.put("current_hp", statistics.getCurrentHp());
        content.put("max_mana", statistics.getMaxMana());
        content.put("current_mana", statistics.getCurrentMana());
        content.put("current_xp", statistics.getCurrentXp());
        content.put("current_level", statistics.getCurrentLevel());
        content.put("strength", statistics.getStrength());
        content.put("dexterity", statistics.getDexterity());
        content.put("constitution", statistics.getConstitution());
        content.put("intelligence", statistics.getIntelligence());
        return new ReportResponse(name, convertToYaml(content));
    }

    @Override
    public ReportResponse visit(Equipment equipment) {
        var name = "Equipment of character " + equipment.getCharacter().getName() + " report";
        var content = new LinkedHashMap<>();
        content.put("gold", equipment.getGold());
        content.put("wood", equipment.getWood());
        content.put("iron", equipment.getIron());
        content.put("armor", equipment.getArmor().getName());
        content.put("weapon", equipment.getWeapon().getName());
        content.put("healthPotions", equipment.getHealthPotions());
        return new ReportResponse(name, convertToYaml(content));
    }

    private String convertToYaml(Map<?, ?> data) {
        Yaml yaml = new Yaml();
        return yaml.dump(data);
    }
}
