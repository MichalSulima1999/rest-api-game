package com.michael1099.rest_rpg.enemy.strategy;

import com.michael1099.rest_rpg.enemy.Enemy;
import com.michael1099.rest_rpg.enemy.strategy.element.StrategyElement;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
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
public class EnemyStrategy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToMany
    @JoinTable(
            name = "strategy_element_enemy_strategy",
            joinColumns = @JoinColumn(name = "enemy_strategy_id"),
            inverseJoinColumns = @JoinColumn(name = "strategy_element_id"))
    private Set<StrategyElement> strategyElements;

    @NotNull
    @OneToOne(mappedBy = "enemyStrategy", fetch = FetchType.EAGER)
    private Enemy enemy;
}
