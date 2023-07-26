package com.michael1099.rest_rpg.statistics;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.exceptions.NotEnoughSkillPointsException;
import com.michael1099.rest_rpg.statistics.dto.StatisticsUpdateRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private int maxHp;

    private int currentHp;

    private int maxMana;

    private int currentMana;

    private int damage;

    private int magicDamage;

    private int armor;

    private float dodgeChance;

    private float criticalChance;

    private int currentXp;

    private int xpToNextLevel;

    private int currentLevel;

    private int freeStatisticPoints;

    private int strength;

    private int dexterity;

    private int constitution;

    private int intelligence;

    @OneToOne(mappedBy = "statistics")
    private Character character;

    private boolean deleted;

    public static Statistics init() {
        return Statistics.builder()
                .maxHp(100)
                .currentHp(100)
                .maxMana(100)
                .currentMana(100)
                .damage(10)
                .magicDamage(10)
                .armor(0)
                .dodgeChance(9.5f)
                .criticalChance(9.5f)
                .currentXp(0)
                .xpToNextLevel(500)
                .currentLevel(1)
                .freeStatisticPoints(10)
                .strength(10)
                .dexterity(10)
                .constitution(10)
                .intelligence(10)
                .build();
    }

    public void addStatistics(@NotNull StatisticsUpdateRequestDto dto) {
        var sum = dto.getConstitution() + dto.getDexterity() + dto.getStrength() + dto.getIntelligence();
        if (sum > freeStatisticPoints) {
            throw new NotEnoughSkillPointsException();
        }

        this.strength += dto.getStrength();
        this.dexterity += dto.getDexterity();
        this.constitution += dto.getConstitution();
        this.intelligence += dto.getIntelligence();
    }
}
