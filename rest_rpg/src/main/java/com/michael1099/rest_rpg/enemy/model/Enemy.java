package com.michael1099.rest_rpg.enemy.model;

import com.michael1099.rest_rpg.adventure.model.Adventure;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.skill.model.Skill;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
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

    private int mana;

    private int damage;

    @Nullable
    @OneToMany(mappedBy = "enemy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Fight> fights = new HashSet<>();

    @Nullable
    @OneToMany(mappedBy = "enemy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Adventure> adventures = new HashSet<>();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "skill_id")
    private Skill skill;

    private int skillLevel;

    private int numberOfPotions;

    @NotEmpty
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "strategy_element_enemy",
            joinColumns = @JoinColumn(name = "enemy_id"),
            inverseJoinColumns = @JoinColumn(name = "strategy_element_id"))
    private Set<StrategyElement> strategyElements = new HashSet<>();

    private boolean deleted;

    private Enemy(MyEnemyBuilder enemyBuilder) {
        this.id = enemyBuilder.id;
        this.name = enemyBuilder.name;
        this.hp = enemyBuilder.hp;
        this.mana = enemyBuilder.mana;
        this.damage = enemyBuilder.damage;
        this.fights = enemyBuilder.fights;
        this.adventures = enemyBuilder.adventures;
        this.skill = enemyBuilder.skill;
        this.skillLevel = enemyBuilder.skillLevel;
        this.numberOfPotions = enemyBuilder.numberOfPotions;
        this.strategyElements = enemyBuilder.strategyElements;
        this.deleted = enemyBuilder.deleted;
    }

    public static Enemy of(@NotNull @Valid EnemyCreateRequestDto dto, @NotNull Skill skill) {
        return new MyEnemyBuilder(
                dto.getName(), dto.getHp(), dto.getMana(), dto.getDamage(), skill, dto.getSkillLevel(), dto.getNumberOfPotions()).build();
    }

    public void usePotion() {
        numberOfPotions = Math.max(0, numberOfPotions - 1);
    }

    // Tydzień 2, Builder
    // Stworzenie klasy builder
    public static class MyEnemyBuilder {

        private final String name;
        private final int hp;
        private final int mana;
        private final int damage;
        private final int skillLevel;
        private final int numberOfPotions;
        private final Skill skill;
        private Long id;
        private Set<Fight> fights;
        private Set<Adventure> adventures;
        private Set<StrategyElement> strategyElements = new HashSet<>();
        private boolean deleted;

        public MyEnemyBuilder(String name, int hp, int mana, int damage, Skill skill, int skillLevel, int numberOfPotions) {
            this.name = name;
            this.hp = hp;
            this.mana = mana;
            this.damage = damage;
            this.skill = skill;
            this.skillLevel = skillLevel;
            this.numberOfPotions = numberOfPotions;
        }

        public MyEnemyBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public MyEnemyBuilder setFights(Set<Fight> fights) {
            this.fights = fights;
            return this;
        }

        public MyEnemyBuilder setAdventures(Set<Adventure> adventures) {
            this.adventures = adventures;
            return this;
        }

        public MyEnemyBuilder setStrategyElements(Set<StrategyElement> strategyElements) {
            this.strategyElements = strategyElements;
            return this;
        }

        public MyEnemyBuilder setDeleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public Enemy build() {
            return new Enemy(this);
        }
    }
    // Koniec Tydzień 2, Builder
}
