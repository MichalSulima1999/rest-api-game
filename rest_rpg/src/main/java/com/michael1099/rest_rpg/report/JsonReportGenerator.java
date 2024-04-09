package com.michael1099.rest_rpg.report;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.character_skill.CharacterSkill;
import com.michael1099.rest_rpg.equipment.Equipment;
import com.michael1099.rest_rpg.skill.model.Skill;
import com.michael1099.rest_rpg.statistics.Statistics;
import org.json.JSONObject;
import org.openapitools.model.ReportResponse;
import org.springframework.stereotype.Service;

@Service
public class JsonReportGenerator implements ReportVisitor {

    @Override
    public ReportResponse visit(Character character) {
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
    public ReportResponse visit(Statistics statistics) {
        var name = "Statistics of character " + statistics.getCharacter().getName() + " report";
        var content = new JSONObject()
                .put("max_hp", statistics.getMaxHp())
                .put("current_hp", statistics.getCurrentHp())
                .put("max_mana", statistics.getMaxMana())
                .put("current_mana", statistics.getCurrentMana())
                .put("current_xp", statistics.getCurrentXp())
                .put("current_level", statistics.getCurrentLevel())
                .put("strength", statistics.getStrength())
                .put("dexterity", statistics.getDexterity())
                .put("constitution", statistics.getConstitution())
                .put("intelligence", statistics.getIntelligence());
        return new ReportResponse(name, content.toString());
    }

    @Override
    public ReportResponse visit(Equipment equipment) {
        var name = "Equipment of character " + equipment.getCharacter().getName() + " report";
        var content = new JSONObject()
                .put("gold", equipment.getGold())
                .put("wood", equipment.getWood())
                .put("iron", equipment.getIron())
                .put("armor", equipment.getArmor().getName())
                .put("weapon", equipment.getWeapon().getName())
                .put("healthPotions", equipment.getHealthPotions());
        return new ReportResponse(name, content.toString());
    }
}
