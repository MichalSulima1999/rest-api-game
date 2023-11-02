package com.michael1099.rest_rpg.fight;

import com.michael1099.rest_rpg.enemy.model.Enemy;
import com.michael1099.rest_rpg.fight_effect.FightEffect;
import com.michael1099.rest_rpg.occupation.Occupation;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Fight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @OneToOne(mappedBy = "fight", fetch = FetchType.EAGER)
    private Occupation occupation;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enemy_id")
    private Enemy enemy;

    @Nullable
    private Integer enemyCurrentHp;

    @Nullable
    private Integer enemyCurrentMana;

    @Nullable
    @OneToMany(mappedBy = "fightEnemy", fetch = FetchType.LAZY)
    private Set<FightEffect> enemyEffects;

    @Nullable
    @OneToMany(mappedBy = "fightPlayer", fetch = FetchType.LAZY)
    private Set<FightEffect> playerEffects;

    @Nullable
    private Boolean playerTurn;

    private boolean isActive;

    private boolean deleted;
}
