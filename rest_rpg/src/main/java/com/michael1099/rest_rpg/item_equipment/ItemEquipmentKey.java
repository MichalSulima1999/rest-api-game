package com.michael1099.rest_rpg.item_equipment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class ItemEquipmentKey {

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "equipment_id")
    private Long equipmentId;
}
