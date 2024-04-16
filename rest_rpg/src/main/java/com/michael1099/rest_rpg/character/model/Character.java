package com.michael1099.rest_rpg.character.model;

import com.michael1099.rest_rpg.auth.user.User;
import com.michael1099.rest_rpg.character.model.dto.CharacterCreateRequestDto;
import com.michael1099.rest_rpg.character_skill.CharacterSkill;
import com.michael1099.rest_rpg.equipment.Equipment;
import com.michael1099.rest_rpg.item.ItemServiceImplementation;
import com.michael1099.rest_rpg.item.model.Item;
import com.michael1099.rest_rpg.occupation.Occupation;
import com.michael1099.rest_rpg.report.ReportVisitor;
import com.michael1099.rest_rpg.report.Reportable;
import com.michael1099.rest_rpg.statistics.Statistics;
import com.michael1099.rest_rpg.statistics.observer.LevelUpObserver;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.model.CharacterClass;
import org.openapitools.model.CharacterRace;
import org.openapitools.model.CharacterSex;
import org.openapitools.model.CharacterStatus;
import org.openapitools.model.ItemType;
import org.openapitools.model.ReportResponse;
import org.openapitools.model.ResourceType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// Tydzień 2, Wzorzec Builder
// Dodana odpowiednia adnotacja do klasy
@Builder
// Koniec, Tydzień 2, Wzorzec Builder
@Table(name = "character_table")
@NamedEntityGraph(name = Character.CHARACTER_BASIC,
        attributeNodes = {
                @NamedAttributeNode("statistics"),
                @NamedAttributeNode(value = "occupation", subgraph = "occupation-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "occupation-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("adventure"),
                                @NamedAttributeNode("work")
                        }
                )
        }
)
@NamedEntityGraph(name = Character.CHARACTER_TEST,
        attributeNodes = {
                @NamedAttributeNode("statistics"),
                @NamedAttributeNode(value = "skills", subgraph = "skills-subgraph"),
                @NamedAttributeNode(value = "equipment", subgraph = "equipment-subgraph"),
                @NamedAttributeNode(value = "occupation", subgraph = "occupation-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "occupation-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode(value = "adventure", subgraph = "adventure-subgraph"),
                                @NamedAttributeNode("work"),
                                @NamedAttributeNode("fight")
                        }
                ),
                @NamedSubgraph(
                        name = "adventure-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("enemy")
                        }
                ),
                @NamedSubgraph(
                        name = "equipment-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("armor"),
                                @NamedAttributeNode("weapon")
                        }
                ),
                @NamedSubgraph(
                        name = "skills-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("skill")
                        }
                )
        }
)
@NamedEntityGraph(name = Character.CHARACTER_FIGHT,
        attributeNodes = {
                @NamedAttributeNode("statistics"),
                @NamedAttributeNode(value = "occupation", subgraph = "occupation-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "occupation-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode(value = "fight", subgraph = "fight-subgraph")
                        }
                ),
                @NamedSubgraph(
                        name = "fight-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("fightEffects"),
                                @NamedAttributeNode(value = "enemy", subgraph = "skill-subgraph")
                        }
                ),
                @NamedSubgraph(
                        name = "skill-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("skill")
                        }
                )
        }
)
@NamedEntityGraph(name = Character.CHARACTER_FIGHT_ACTION,
        attributeNodes = {
                @NamedAttributeNode("statistics"),
                @NamedAttributeNode(value = "occupation", subgraph = "occupation-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "occupation-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode(value = "fight", subgraph = "fight-subgraph")
                        }
                ),
                @NamedSubgraph(
                        name = "fight-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("fightEffects"),
                                @NamedAttributeNode(value = "enemy", subgraph = "enemy-subgraph")
                        }
                ),
                @NamedSubgraph(
                        name = "enemy-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("skill"),
                                @NamedAttributeNode("strategyElements")
                        }
                )
        }
)
@NamedEntityGraph(name = Character.CHARACTER_FIGHT_LITE,
        attributeNodes = {
                @NamedAttributeNode("statistics"),
                @NamedAttributeNode(value = "occupation", subgraph = "occupation-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "occupation-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("fight")
                        }
                )
        }
)
@NamedEntityGraph(name = Character.CHARACTER_SKILLS,
        attributeNodes = {
                @NamedAttributeNode(value = "skills", subgraph = "skills-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "skills-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("skill")
                        }
                )
        }
)
public class Character implements Cloneable, Reportable {

    public static final String CHARACTER_BASIC = "CHARACTER_BASIC_GRAPH";
    public static final String CHARACTER_DETAILS = "CHARACTER_DETAILS_GRAPH";
    public static final String CHARACTER_FIGHT = "CHARACTER_FIGHT_GRAPH";
    public static final String CHARACTER_FIGHT_ACTION = "CHARACTER_FIGHT_ACTION_GRAPH";
    public static final String CHARACTER_FIGHT_LITE = "CHARACTER_FIGHT_LITE_GRAPH";
    public static final String CHARACTER_SKILLS = "CHARACTER_SKILLS_GRAPH";
    public static final String CHARACTER_TEST = "CHARACTER_TEST_GRAPH";

    @Transient
    private List<LevelUpObserver> observers = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotEmpty
    @Column(unique = true)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CharacterRace race;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CharacterSex sex;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CharacterClass characterClass;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CharacterStatus status = CharacterStatus.IDLE;

    @Nullable
    @Enumerated(EnumType.STRING)
    private CharacterArtwork artwork;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "character", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CharacterSkill> skills = new HashSet<>();

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "statistics_id", referencedColumnName = "id")
    private Statistics statistics;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "occupation_id", referencedColumnName = "id")
    private Occupation occupation;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", referencedColumnName = "id")
    private Equipment equipment;

    private boolean deleted;

    public static Character createCharacter(@NotNull @Valid CharacterCreateRequestDto dto, @NotNull User user) {
        // Tydzień 2, Wzorzec Builder
        // Używany jest builder, który pozwala na ustawienie odpowiednich pól, a następnie jest możliwość
        // stworzenia obiektu używając .build()
        var character = Character.builder()
                .name(dto.getName())
                .characterClass(dto.getCharacterClass())
                .artwork(dto.getArtwork())
                .race(dto.getRace())
                .sex(dto.getSex())
                .skills(new HashSet<>())
                .equipment(Equipment.init())
                .occupation(Occupation.init())
                .statistics(Statistics.init())
                .status(CharacterStatus.IDLE)
                .user(user)
                .observers(new ArrayList<>())
                .build();
        // Koniec, Tydzień 2, Wzorzec Builder

        character.getOccupation().setCharacter(character);
        character.getStatistics().setCharacter(character);
        character.getEquipment().setCharacter(character);

        return character;
    }

    public void buyItem(@NotNull Item item) {
        equipment.spendGold(item.getPrice());
        if (item.getType() == ItemType.ARMOR) {
            equipment.setArmor(item);
        } else {
            equipment.setWeapon(item);
        }
    }

    public void buyPotion() {
        equipment.spendGold(ItemServiceImplementation.POTION_PRICE);
        equipment.addPotion(1);
    }

    public void usePotion() {
        equipment.usePotion();
        statistics.getHealOperation().heal(ItemServiceImplementation.POTION_HEAL_PERCENT);
    }

    public void acceptResourcesTask(ResourceType resourceType, int amount) {

    }

    public void acceptMonsterTask(String monsterType, int monstersNumber) {

    }

    public void endResourcesTask() {

    }

    public void endMonsterTask() {

    }

    public void earnXp(int xp) {
        var leveledUp = statistics.getEarnXpOperation().earnXp(xp);

        if (leveledUp) {
            notifyObservers();
        }
    }

    public void addObserver(LevelUpObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(LevelUpObserver observer) {
        observers.remove(observer);
    }

    // Tydzień 2, Wzorzec Prototype
    // Korzystając z interfejsu Cloneable, tworzona jest metoda clone, która tworzy kopię obiektu, następnie ustawiane są
    // wymagane atrybuty powiązanych encji
    @Override
    public Character clone() {
        try {
            Character clone = (Character) super.clone();
            clone.setId(null);
            clone.getEquipment().setId(null);
            clone.getEquipment().setCharacter(clone);
            clone.getOccupation().setId(null);
            clone.getOccupation().setCharacter(clone);
            clone.getOccupation().getFight().setId(null);
            clone.getStatistics().setId(null);
            clone.getStatistics().setCharacter(clone);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    // Koniec, Tydzień 2, Wzorzec Prototype

    @Override
    public ReportResponse accept(ReportVisitor visitor) {
        return visitor.visit(this);
    }

    private void notifyObservers() {
        for (LevelUpObserver observer : observers) {
            observer.onLevelUp(this);
        }
    }
}
