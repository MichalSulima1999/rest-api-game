package com.michael1099.rest_rpg.adventure;

import com.michael1099.rest_rpg.enemy.model.Enemy;
import com.michael1099.rest_rpg.item.Item;
import com.michael1099.rest_rpg.occupation.Occupation;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Adventure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    private String name;

    @Nullable
    private LocalDateTime adventureTime;

    @OneToMany(mappedBy = "adventure")
    private Set<Item> items;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    private Enemy enemy;

    @OneToMany(mappedBy = "adventure")
    private Set<Occupation> occupation;

    private boolean deleted;
}
