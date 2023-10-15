package com.michael1099.rest_rpg.enemy.strategy.element;

import com.michael1099.rest_rpg.enemy.model.Enemy;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.model.ElementAction;
import org.openapitools.model.ElementEvent;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StrategyElement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ElementEvent elementEvent;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ElementAction elementAction;

    @ManyToMany(mappedBy = "strategyElements")
    private Set<Enemy> enemy = new HashSet<>();

    private int priority;
}
