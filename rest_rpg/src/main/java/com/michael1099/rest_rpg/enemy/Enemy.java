package com.michael1099.rest_rpg.enemy;

import com.michael1099.rest_rpg.adventure.Adventure;
import com.michael1099.rest_rpg.enemy.strategy.EnemyStrategy;
import com.michael1099.rest_rpg.fight.Fight;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
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
public class Enemy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    private String name;

    private int hp;

    private int damage;

    @Nullable
    @OneToMany(mappedBy = "enemy", fetch = FetchType.LAZY)
    private Set<Fight> fights;

    @Nullable
    @OneToMany(mappedBy = "enemy")
    private Set<Adventure> adventures;

    private int numberOfPotions;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "enemy_strategy_id", referencedColumnName = "id")
    private EnemyStrategy enemyStrategy;

    private boolean deleted;
}
