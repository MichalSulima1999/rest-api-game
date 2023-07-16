package com.michael1099.rest_rpg.statistics;

import com.michael1099.rest_rpg.character.Character;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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

    private int strength;

    private int dexterity;

    private int constitution;

    private int intelligence;

    @OneToOne(mappedBy = "statistics")
    private Character character;

    private boolean deleted;
}
