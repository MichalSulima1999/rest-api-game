package com.michael1099.rest_rpg.statistics;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.character.model.CharacterRace;
import com.michael1099.rest_rpg.exceptions.EnumValueNotFoundException;
import com.michael1099.rest_rpg.exceptions.NotEnoughSkillPointsException;
import com.michael1099.rest_rpg.statistics.dto.StatisticsUpdateRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Statistics {

    public static final int HP_MULTIPLIER = 10;
    public static final int MANA_MULTIPLIER = 10;
    public static final int DAMAGE_MULTIPLIER = 10;
    public static final int MAGIC_DAMAGE_MULTIPLIER = 10;
    public static final int START_FREE_STATISTICS_POINTS = 50;
    public static final int CHARACTER_RACE_BONUS = 5;

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
                .freeStatisticPoints(START_FREE_STATISTICS_POINTS)
                .strength(10)
                .dexterity(10)
                .constitution(10)
                .intelligence(10)
                .build();
    }

    public void addStatistics(@NotNull StatisticsUpdateRequestDto dto, @NotNull CharacterRace race) {
        var sum = dto.getConstitution() + dto.getDexterity() + dto.getStrength() + dto.getIntelligence();
        if (sum > freeStatisticPoints) {
            throw new NotEnoughSkillPointsException();
        }

        this.strength = dto.getStrength();
        this.dexterity = dto.getDexterity();
        this.constitution = dto.getConstitution();
        this.intelligence = dto.getIntelligence();
        this.freeStatisticPoints -= dto.getStrength() + dto.getDexterity() + dto.getConstitution() + dto.getIntelligence();
        updateStats();
        setRaceBonus(race);
    }

    public void updateStats() {
        // + items in future
        this.dodgeChance = criticalDodgeChance(this.dexterity);
        this.criticalChance = criticalDodgeChance(this.dexterity);
        var previousMaxHp = this.maxHp;
        this.maxHp = this.constitution * HP_MULTIPLIER;
        this.currentHp += this.maxHp - previousMaxHp;
        var previousMaxMana = this.maxMana;
        this.maxMana = this.intelligence * MANA_MULTIPLIER;
        this.currentMana += this.maxMana - previousMaxMana;
        this.damage = this.strength * DAMAGE_MULTIPLIER;
        this.magicDamage = this.intelligence * MAGIC_DAMAGE_MULTIPLIER;
    }

    private void setRaceBonus(CharacterRace race) {
        switch (race) {
            case ELF -> this.dexterity += CHARACTER_RACE_BONUS;
            case DWARF -> this.constitution += CHARACTER_RACE_BONUS;
            case HUMAN -> this.strength += CHARACTER_RACE_BONUS;
            default -> throw new EnumValueNotFoundException();
        }
    }

    private float criticalDodgeChance(int statistic) {
        double k = 0.01;
        return (float) (100 * (1 - Math.exp(-k * statistic)));
    }
}
