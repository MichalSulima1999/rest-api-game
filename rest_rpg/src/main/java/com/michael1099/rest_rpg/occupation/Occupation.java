package com.michael1099.rest_rpg.occupation;

import com.michael1099.rest_rpg.adventure.Adventure;
import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.training.Training;
import com.michael1099.rest_rpg.work.Work;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Occupation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Nullable
    private LocalDateTime finishTime;

    @NotNull
    @OneToOne(mappedBy = "occupation", fetch = FetchType.EAGER)
    private Character character;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adventure_id")
    private Adventure adventure;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id")
    private Training training;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_id")
    private Work work;

    private boolean deleted;

    public boolean isOccupied() {
        return adventure != null ||
                training != null ||
                work != null;
    }

    public String getOccupationType() {
        if (adventure != null) {
            return adventure.getName();
        }
        if (training != null) {
            return training.getName();
        }
        if (work != null) {
            return work.getName();
        }
        return null;
    }
}
