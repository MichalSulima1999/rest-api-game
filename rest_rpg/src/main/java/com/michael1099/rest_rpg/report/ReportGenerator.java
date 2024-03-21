package com.michael1099.rest_rpg.report;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.character_skill.CharacterSkill;
import com.michael1099.rest_rpg.equipment.Equipment;
import com.michael1099.rest_rpg.skill.model.Skill;
import com.michael1099.rest_rpg.statistics.Statistics;
import org.openapitools.model.ReportResponse;
import org.springframework.stereotype.Service;

@Service
public class ReportGenerator implements ReportVisitor {

    @Override
    public ReportResponse visit(Character character) {
        var name = "Character " + character.getName() + " report";
        var skills = character.getSkills().stream().map(CharacterSkill::getSkill).map(Skill::getName).toList();
        var content = "Race: " + character.getRace() +
                "sex: " + character.getSex() +
                "class: " + character.getCharacterClass().toString() +
                "skills: " + skills;
        return new ReportResponse(name, content);
    }

    @Override
    public ReportResponse visit(Statistics statistics) {
        var name = "Statistics of character " + statistics.getCharacter().getName() + " report";
        var content = "Max hp: " + statistics.getMaxHp() +
                "Current hp: " + statistics.getCurrentHp() +
                "Max mana: " + statistics.getMaxMana() +
                "Current mana: " + statistics.getCurrentMana() +
                "Current xp: " + statistics.getCurrentXp() +
                "Current level: " + statistics.getCurrentLevel() +
                "strength: " + statistics.getStrength() +
                "dexterity: " + statistics.getDexterity() +
                "constitution: " + statistics.getConstitution() +
                "intelligence: " + statistics.getIntelligence();
        return new ReportResponse(name, content);
    }

    @Override
    public ReportResponse visit(Equipment equipment) {
        var name = "Equipment of character " + equipment.getCharacter().getName() + " report";
        var content = "gold: " + equipment.getGold() +
                "wood: " + equipment.getWood() +
                "iron: " + equipment.getIron() +
                "armor: " + equipment.getArmor().getName() +
                "weapon: " + equipment.getWeapon().getName() +
                "healthPotions: " + equipment.getHealthPotions();
        return new ReportResponse(name, content);
    }
}
