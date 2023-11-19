package com.michael1099.rest_rpg.equipment

import com.michael1099.rest_rpg.item.ItemHelper
import org.openapitools.model.EquipmentDetails

class EquipmentHelper {

    static boolean compare(Equipment equipment, EquipmentDetails details) {
        assert equipment.id == details.id
        assert equipment.gold == details.gold
        assert equipment.healthPotions == details.healthPotions
        assert !equipment.armor || ItemHelper.compare(equipment.armor, details.armor)
        assert !equipment.weapon || ItemHelper.compare(equipment.weapon, details.weapon)

        true
    }
}
