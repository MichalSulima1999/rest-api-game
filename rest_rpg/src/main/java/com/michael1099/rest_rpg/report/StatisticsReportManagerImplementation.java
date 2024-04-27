package com.michael1099.rest_rpg.report;

import com.michael1099.rest_rpg.statistics.Statistics;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.openapitools.model.ReportResponse;

import java.util.LinkedHashMap;

import static com.michael1099.rest_rpg.report.YamlReportGenerator.convertToYaml;

@RequiredArgsConstructor
public class StatisticsReportManagerImplementation implements ReportManager {

    private final Statistics statistics;

    @Override
    public ReportResponse generateDefaultReport() {
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
    public ReportResponse generateJsonReport() {
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
    public ReportResponse generateYamlReport() {
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
}
