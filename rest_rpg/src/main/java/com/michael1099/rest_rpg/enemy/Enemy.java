package com.michael1099.rest_rpg.enemy;

import com.michael1099.rest_rpg.adventure.Adventure;
import com.michael1099.rest_rpg.fight.Fight;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @Nullable
    @OneToMany(mappedBy = "enemy", fetch = FetchType.LAZY)
    private Set<Fight> fights;

    @OneToMany(mappedBy = "enemy")
    private Set<Adventure> adventures;

    private boolean deleted;
}
