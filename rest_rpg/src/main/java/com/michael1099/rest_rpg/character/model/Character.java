package com.michael1099.rest_rpg.character.model;

import com.michael1099.rest_rpg.auth.user.User;
import com.michael1099.rest_rpg.character.model.dto.CharacterCreateRequestDto;
import com.michael1099.rest_rpg.character_skill.CharacterSkill;
import com.michael1099.rest_rpg.equipment.Equipment;
import com.michael1099.rest_rpg.occupation.Occupation;
import com.michael1099.rest_rpg.statistics.Statistics;
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

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
                                @NamedAttributeNode("training"),
                                @NamedAttributeNode("work")
                        }
                )
        }
)
@NamedEntityGraph(name = Character.CHARACTER_TEST,
        attributeNodes = {
                @NamedAttributeNode("statistics"),
                @NamedAttributeNode("skills"),
                @NamedAttributeNode(value = "occupation", subgraph = "occupation-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "occupation-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode(value = "adventure", subgraph = "adventure-subgraph"),
                                @NamedAttributeNode("training"),
                                @NamedAttributeNode("work"),
                                @NamedAttributeNode("fight")
                        }
                ),
                @NamedSubgraph(
                        name = "adventure-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("enemy")
                        }
                )
        }
)
public class Character {

    public static final String CHARACTER_BASIC = "CHARACTER_BASIC_GRAPH";
    public static final String CHARACTER_TEST = "CHARACTER_TEST_GRAPH";

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

    @OneToMany(mappedBy = "character", fetch = FetchType.LAZY)
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
        var character = Character.builder()
                .name(dto.getName())
                .characterClass(dto.getCharacterClass())
                .artwork(dto.getArtwork())
                .race(dto.getRace())
                .sex(dto.getSex())
                .equipment(Equipment.init())
                .occupation(Occupation.init())
                .statistics(Statistics.init())
                .status(CharacterStatus.IDLE)
                .user(user)
                .build();

        character.getOccupation().setCharacter(character);
        character.getStatistics().setCharacter(character);
        character.getEquipment().setCharacter(character);

        return character;
    }
}
