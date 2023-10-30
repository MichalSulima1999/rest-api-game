package com.michael1099.rest_rpg.fight_effect;

import com.michael1099.rest_rpg.fight.Fight;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.openapitools.model.SkillEffect;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FightEffect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fight_player_id")
    private Fight fightPlayer;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fight_enemy_id")
    private Fight fightEnemy;

    @Nullable
    @Enumerated(EnumType.STRING)
    private SkillEffect skillEffect;

    @Nullable
    private Integer duration;
}
