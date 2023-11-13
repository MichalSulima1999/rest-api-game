package com.michael1099.rest_rpg.fight

import com.michael1099.rest_rpg.adventure.AdventureServiceHelper
import com.michael1099.rest_rpg.character.CharacterServiceHelper
import com.michael1099.rest_rpg.character.model.Character
import com.michael1099.rest_rpg.configuration.TestBase
import com.michael1099.rest_rpg.enemy.EnemyServiceHelper
import com.michael1099.rest_rpg.equipment.Equipment
import com.michael1099.rest_rpg.fight.model.Fight
import com.michael1099.rest_rpg.fight_effect.FightEffect
import com.michael1099.rest_rpg.helpers.DeleteServiceHelper
import com.michael1099.rest_rpg.item.ItemService
import com.michael1099.rest_rpg.skill.SkillServiceHelper
import com.michael1099.rest_rpg.statistics.StatisticsHelper
import org.openapitools.model.ElementAction
import org.openapitools.model.FightActionRequest
import org.openapitools.model.FightActionResponse
import org.openapitools.model.FightDetails
import org.openapitools.model.SkillEffect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class FightControllerTest extends TestBase {

    def baseUrl = "/fight"
    def fightUrl = { long characterId -> baseUrl + "/" + characterId }

    @Autowired
    CharacterServiceHelper characterServiceHelper

    @Autowired
    FightServiceHelper fightServiceHelper

    @Autowired
    EnemyServiceHelper enemyServiceHelper

    @Autowired
    DeleteServiceHelper deleteServiceHelper

    @Autowired
    SkillServiceHelper skillServiceHelper

    @Autowired
    AdventureServiceHelper adventureServiceHelper

    void cleanup() {
        deleteServiceHelper.clean()
    }

    def "should get fight"() {
        given:
            def character = characterServiceHelper.createCharacter(user, [name: "Carl"])
            def enemy = enemyServiceHelper.saveEnemy()
            def effects = [fightServiceHelper.saveFightEffect(fight: character.occupation.fight),
                           fightServiceHelper.saveFightEffect(fight: character.occupation.fight, isPlayerEffect: false)] as Set
            def fight = character.occupation.fight
            fight.setFightEffects(effects)
            fight.setEnemy(enemy)
            fight.setEnemyCurrentHp(100)
            fight.setEnemyCurrentMana(100)
            fight = fightServiceHelper.save(fight)
        when:
            def response = httpGet(fightUrl(character.id), FightDetails, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.OK
            FightHelper.compare(fight, response.body)
    }

    def "should attack normal"() {
        given:
            def character = characterServiceHelper.createCharacter(user, [name: "Carl", strength: 10, dexterity: 0])
            def adventure = adventureServiceHelper.saveAdventure()
            character.occupation.setAdventure(adventure)
            character = characterServiceHelper.save(character)
            def effect = [fightServiceHelper.saveFightEffect(
                    skillEffect: SkillEffect.WEAKNESS,
                    fight: character.occupation.fight,
                    effectMultiplier: 0.5,
                    duration: 3,
                    playerEffect: true)] as Set
            def fight = prepareFight(character, effect)
        and:
            def request = new FightActionRequest(character.id, ElementAction.NORMAL_ATTACK.toString())
        when:
            def response = httpPost(baseUrl, request, FightActionResponse, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.OK
            FightHelper.compare(fightServiceHelper.getById(fight.id), response.body.fight)
            response.body.playerDamage == character.statistics.damage * 0.5 * (response.body.playerCriticalStrike ? 2 : 1)
    }

    def "should attack special"() {
        given:
            def skill = skillServiceHelper.createSkill()
            def character = characterServiceHelper.createCharacter(user, [name: "Carl", skills: [skill]])
            def adventure = adventureServiceHelper.saveAdventure()
            character.occupation.setAdventure(adventure)
            character = characterServiceHelper.save(character)
            def fight = prepareFight(character)
        and:
            def request = new FightActionRequest(character.id, ElementAction.SPECIAL_ATTACK.toString()).skillId(skill.id)
        when:
            def response = httpPost(baseUrl, request, FightActionResponse, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.OK
            response.body.with {
                FightHelper.compare(fightServiceHelper.getById(fight.id), it.fight)
                assert it.playerCurrentMana == character.statistics.maxMana - skill.manaCost
                assert it.playerCurrentHp
            }
    }

    def "should use potion"() {
        given:
            def character = characterServiceHelper.createCharacter(user, [
                    equipment : Equipment.builder().healthPotions(1).build(),
                    statistics: StatisticsHelper.statistics(maxHp: 100, currentHp: 50)])
            def adventure = adventureServiceHelper.saveAdventure()
            character.occupation.setAdventure(adventure)
            character = characterServiceHelper.save(character)
            def fight = prepareFight(character)
            fight.setEnemyCurrentHp(10)
            fight = fightServiceHelper.save(fight)
        and:
            def request = new FightActionRequest(character.id, ElementAction.USE_POTION.toString())
        when:
            def response = httpPost(baseUrl, request, FightActionResponse, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.OK
            response.body.every {
                FightHelper.compare(fightServiceHelper.getById(fight.id), it.fight)
                it.playerPotions == 0
                it.playerCurrentHp == ItemService.POTION_HEAL_PERCENT * character.statistics.maxHp / 100 + character.statistics.currentHp
            }
    }

    def "should win fight"() {
        given:
            def character = characterServiceHelper.createCharacter(user, [
                    statistics: StatisticsHelper.statistics(currentXp: 0, currentLevel: 1),
                    equipment : Equipment.builder().gold(0).build(),
            ])
            def adventure = adventureServiceHelper.saveAdventure()
            character.occupation.setAdventure(adventure)
            character = characterServiceHelper.save(character)
            def fight = prepareFight(character)
            fight.setEnemyCurrentHp(10)
            fight = fightServiceHelper.save(fight)
        and:
            def request = new FightActionRequest(character.id, ElementAction.NORMAL_ATTACK.toString())
        when:
            def response = httpPost(baseUrl, request, FightActionResponse, [accessToken: userAccessToken])
            character = characterServiceHelper.getCharacter(character.id)
        then:
            response.status == HttpStatus.OK
            FightHelper.compare(fightServiceHelper.getById(fight.id), response.body.fight)
            response.body.playerWon == true
            character.getOccupation().adventure == null
            character.statistics.currentXp == adventure.xpForAdventure
            character.equipment.gold == adventure.goldForAdventure
    }

    def "should lose fight"() {
        given:
            def character = characterServiceHelper.createCharacter(user, [
                    statistics: StatisticsHelper.statistics(currentXp: 0, currentLevel: 1, currentHp: 0, strength: 1),
                    equipment : Equipment.builder().gold(0).build(),
            ])
            def adventure = adventureServiceHelper.saveAdventure()
            character.occupation.setAdventure(adventure)
            character = characterServiceHelper.save(character)
            def fight = prepareFight(character)
        and:
            def request = new FightActionRequest(character.id, ElementAction.NORMAL_ATTACK.toString())
        when:
            def response = httpPost(baseUrl, request, FightActionResponse, [accessToken: userAccessToken])
            character = characterServiceHelper.getCharacter(character.id)
        then:
            response.status == HttpStatus.OK
            FightHelper.compare(fightServiceHelper.getById(fight.id), response.body.fight)
            response.body.playerWon == false
            character.getOccupation().adventure == null
    }

    def "should kill player due to a bleeding effect"() {
        given:
            def character = characterServiceHelper.createCharacter(user, [
                    statistics: StatisticsHelper.statistics(currentXp: 0, currentLevel: 1, currentHp: 1, strength: 1),
                    equipment : Equipment.builder().gold(0).build(),
            ])
            def adventure = adventureServiceHelper.saveAdventure()
            character.occupation.setAdventure(adventure)
            character = characterServiceHelper.save(character)
            def bleedingEffect = [fightServiceHelper.saveFightEffect(
                    skillEffect: SkillEffect.BLEEDING,
                    fight: character.occupation.fight,
                    effectMultiplier: 0.2,
                    duration: 3,
                    playerEffect: true)] as Set
            def fight = prepareFight(character, bleedingEffect)
        and:
            def request = new FightActionRequest(character.id, ElementAction.NORMAL_ATTACK.toString())
        when:
            def response = httpPost(baseUrl, request, FightActionResponse, [accessToken: userAccessToken])
            character = characterServiceHelper.getCharacter(character.id)
        then:
            response.status == HttpStatus.OK
            FightHelper.compare(fightServiceHelper.getById(fight.id), response.body.fight)
            response.body.playerWon == false
            character.getOccupation().adventure == null
    }

    def "should kill enemy due to a bleeding effect"() {
        given:
            def character = characterServiceHelper.createCharacter(user, [
                    statistics: StatisticsHelper.statistics(currentXp: 0, currentLevel: 1, currentHp: 100, strength: 1),
                    equipment : Equipment.builder().healthPotions(1).build(),
            ])
            def adventure = adventureServiceHelper.saveAdventure()
            character.occupation.setAdventure(adventure)
            character = characterServiceHelper.save(character)
            def bleedingEffect = [fightServiceHelper.saveFightEffect(
                    skillEffect: SkillEffect.BLEEDING,
                    fight: character.occupation.fight,
                    effectMultiplier: 0.2,
                    duration: 3,
                    playerEffect: false)] as Set
            def fight = prepareFight(character, bleedingEffect)
            fight.setEnemyCurrentHp(5)
            fight = fightServiceHelper.save(fight)
        and:
            def request = new FightActionRequest(character.id, ElementAction.USE_POTION.toString())
        when:
            def response = httpPost(baseUrl, request, FightActionResponse, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.OK
            FightHelper.compare(fightServiceHelper.getById(fight.id), response.body.fight)
            response.body.playerWon == true
            response.body.fight.enemyCurrentHp == 0
            response.body.fight.fightEffects.first().duration == 0
    }

    def "player should not perform an action due to being stunned"() {
        given:
            def character = characterServiceHelper.createCharacter(user, [
                    statistics: StatisticsHelper.statistics(currentXp: 0, currentLevel: 1, currentHp: 100, strength: 1),
                    equipment : Equipment.builder().healthPotions(1).build(),
            ])
            def adventure = adventureServiceHelper.saveAdventure()
            character.occupation.setAdventure(adventure)
            character = characterServiceHelper.save(character)
            def effect = [fightServiceHelper.saveFightEffect(
                    skillEffect: SkillEffect.STUNNED,
                    fight: character.occupation.fight,
                    effectMultiplier: 0.2,
                    duration: 3,
                    playerEffect: true)] as Set
            def fight = prepareFight(character, effect)
        and:
            def request = new FightActionRequest(character.id, ElementAction.USE_POTION.toString())
        when:
            def response = httpPost(baseUrl, request, FightActionResponse, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.OK
            FightHelper.compare(fightServiceHelper.getById(fight.id), response.body.fight)
            response.body.playerPotions == 1
            response.body.fight.fightEffects.first().duration == 2
    }

    def "enemy should not perform an action due to being stunned"() {
        given:
            def character = characterServiceHelper.createCharacter(user, [
                    statistics: StatisticsHelper.statistics(currentXp: 0, currentLevel: 1, currentHp: 1, strength: 1),
                    equipment : Equipment.builder().healthPotions(1).build(),
            ])
            def adventure = adventureServiceHelper.saveAdventure()
            character.occupation.setAdventure(adventure)
            character = characterServiceHelper.save(character)
            def effect = [fightServiceHelper.saveFightEffect(
                    skillEffect: SkillEffect.STUNNED,
                    fight: character.occupation.fight,
                    duration: 3,
                    playerEffect: false)] as Set
            def fight = prepareFight(character, effect)
            fight.setEnemyCurrentHp(5)
            fight = fightServiceHelper.save(fight)
        and:
            def request = new FightActionRequest(character.id, ElementAction.USE_POTION.toString())
        when:
            def response = httpPost(baseUrl, request, FightActionResponse, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.OK
            FightHelper.compare(fightServiceHelper.getById(fight.id), response.body.fight)
            response.body.fight.enemyCurrentHp == 5
    }

    private Fight prepareFight(Character character, Set<FightEffect> fightEffects = []) {
        def enemy = enemyServiceHelper.saveEnemy()
        def fight = character.occupation.fight
        fight.setFightEffects(fightEffects)
        fight.setEnemy(enemy)
        fight.setActive(true)
        fight.setEnemyCurrentHp(200)
        fight.setEnemyCurrentMana(100)
        fightServiceHelper.save(fight)
    }
}
