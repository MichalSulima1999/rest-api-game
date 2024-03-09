package com.michael1099.rest_rpg.occupation;

import com.michael1099.rest_rpg.adventure.model.Adventure;
import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.exceptions.CharacterIsOccupiedException;
import com.michael1099.rest_rpg.exceptions.EnumValueNotFoundException;
import com.michael1099.rest_rpg.fight.model.Fight;
import com.michael1099.rest_rpg.work.EndWork;
import com.michael1099.rest_rpg.work.EndWorkWithGold;
import com.michael1099.rest_rpg.work.EndWorkWithIron;
import com.michael1099.rest_rpg.work.EndWorkWithWood;
import com.michael1099.rest_rpg.work.model.Work;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
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
    @OneToOne(mappedBy = "occupation", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Character character;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adventure_id")
    private Adventure adventure;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_id")
    private Work work;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "fight_id", referencedColumnName = "id")
    private Fight fight;

    private boolean deleted;

    public static Occupation init() {
        var occupation = new Occupation();
        occupation.setFight(new Fight());
        occupation.getFight().setOccupation(occupation);
        return occupation;
    }

    public boolean isOccupied() {
        return adventure != null ||
                work != null ||
                fight.isActive();
    }

    public void throwIfCharacterIsOccupied() {
        if (isOccupied()) {
            throw new CharacterIsOccupiedException();
        }
    }

    public String getOccupationType() {
        if (fight.isActive()) {
            return Fight.class.getSimpleName();
        }
        if (adventure != null) {
            return Adventure.class.getSimpleName();
        }
        if (work != null) {
            return Work.class.getSimpleName();
        }
        return null;
    }

    public void startAdventure(@NotNull Adventure adventure) {
        setAdventure(adventure);
        setFinishTime(LocalDateTime.now().plusMinutes(adventure.getAdventureTimeInMinutes()));
    }

    public void endAdventure(@NotNull Adventure adventure) {
        var enemy = adventure.getEnemy();
        getFight().setEnemy(enemy);
        getFight().setActive(true);
        getFight().setEnemyCurrentHp(enemy.getHp());
        getFight().setEnemyCurrentMana(enemy.getMana());
    }

    public void startWork(@NotNull Work work) {
        setWork(work);
        setFinishTime(LocalDateTime.now().plusMinutes(work.getWorkMinutes()));
    }

    public void endWork(@NotNull Work work) {
        // Tydzień 6, Template
        // Stworzona została abstrakcyjna klasa EndWork, która rozszerza EndWorkWithGold, EndWorkWithIron i EndWorkWithWood
        // Metoda getResources() jest nadpisywana w tych klasach
        EndWork endWork;
        switch (work.getResourceType()) {
            case GOLD -> endWork = new EndWorkWithGold(this, character.getEquipment(), work);
            case IRON -> endWork = new EndWorkWithIron(this, character.getEquipment(), work);
            case WOOD -> endWork = new EndWorkWithWood(this, character.getEquipment(), work);
            default -> throw new EnumValueNotFoundException();
        }
        // Koniec Tydzień 6, Template
        endWork.finishWork();
    }
}
