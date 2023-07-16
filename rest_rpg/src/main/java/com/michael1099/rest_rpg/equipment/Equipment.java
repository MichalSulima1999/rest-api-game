package com.michael1099.rest_rpg.equipment;

import com.michael1099.rest_rpg.character.Character;
import com.michael1099.rest_rpg.item_equipment.ItemEquipment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Min(0)
    private int gold;

    @NotNull
    @OneToOne(mappedBy = "equipment", fetch = FetchType.EAGER)
    private Character character;

    @OneToMany(mappedBy = "equipment", fetch = FetchType.LAZY)
    private Set<ItemEquipment> items;

    private boolean deleted;
}
