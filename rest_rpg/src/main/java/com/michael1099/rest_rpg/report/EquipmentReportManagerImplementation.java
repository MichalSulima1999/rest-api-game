package com.michael1099.rest_rpg.report;

import com.michael1099.rest_rpg.equipment.Equipment;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.openapitools.model.ReportResponse;

import java.util.LinkedHashMap;

import static com.michael1099.rest_rpg.report.YamlReportGenerator.convertToYaml;

@RequiredArgsConstructor
public class EquipmentReportManagerImplementation implements ReportManager {

    private final Equipment equipment;

    @Override
    public ReportResponse generateDefaultReport() {
        var name = "Equipment of character " + equipment.getCharacter().getName() + " report";
        var content = "gold: " + equipment.getGold() +
                "wood: " + equipment.getWood() +
                "iron: " + equipment.getIron() +
                "armor: " + equipment.getArmor().getName() +
                "weapon: " + equipment.getWeapon().getName() +
                "healthPotions: " + equipment.getHealthPotions();
        return new ReportResponse(name, content);
    }

    @Override
    public ReportResponse generateJsonReport() {
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

    @Override
    public ReportResponse generateYamlReport() {
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
}
