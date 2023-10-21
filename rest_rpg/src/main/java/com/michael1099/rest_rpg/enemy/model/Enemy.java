package com.michael1099.rest_rpg.enemy.model;

import com.michael1099.rest_rpg.adventure.Adventure;
import com.michael1099.rest_rpg.fight.Fight;
import com.michael1099.rest_rpg.skill.model.Skill;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Enemy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    private String name;

    private int hp;

    private int mana;

    private int damage;

    @Nullable
    @OneToMany(mappedBy = "enemy", fetch = FetchType.LAZY)
    private Set<Fight> fights;

    @Nullable
    @OneToMany(mappedBy = "enemy", fetch = FetchType.LAZY)
    private Set<Adventure> adventures = new HashSet<>();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "skill_id")
    private Skill skill;

    private int skillLevel;

    private int numberOfPotions;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "strategy_element_enemy",
            joinColumns = @JoinColumn(name = "enemy_id"),
            inverseJoinColumns = @JoinColumn(name = "strategy_element_id"))
    private Set<StrategyElement> strategyElements = new HashSet<>();

    private boolean deleted;

    public static Enemy of(@NotNull @Valid EnemyCreateRequestDto dto, @NotNull Skill skill) {
        return Enemy.builder()
                .hp(dto.getHp())
                .mana(dto.getMana())
                .damage(dto.getDamage())
                .name(dto.getName())
                .numberOfPotions(dto.getNumberOfPotions())
                .skill(skill)
                .deleted(false)
                .build();
    }
}
