package com.michael1099.rest_rpg.item

import com.michael1099.rest_rpg.character.CharacterServiceHelper
import com.michael1099.rest_rpg.configuration.TestBase
import com.michael1099.rest_rpg.equipment.Equipment
import com.michael1099.rest_rpg.helpers.DeleteServiceHelper
import com.michael1099.rest_rpg.statistics.StatisticsHelper
import org.openapitools.model.ErrorCodes
import org.openapitools.model.ItemLite
import org.openapitools.model.ItemLitePage
import org.openapitools.model.ItemType
import org.openapitools.model.PotionLite
import org.openapitools.model.StatisticsLite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class ItemControllerTest extends TestBase {

    def baseUrl = "/item"
    def searchUrl = baseUrl + "/search"
    def buyItemUrl = { long itemId, long characterId -> baseUrl + "/" + itemId + "/buy/" + characterId }
    def buyPotionUrl = { long characterId -> baseUrl + "/potion/buy/" + characterId }
    def usePotionUrl = { long characterId -> baseUrl + "/potion/use/" + characterId }
    def potionInfoUrl = baseUrl + "/potion/info"

    @Autowired
    DeleteServiceHelper deleteServiceHelper

    @Autowired
    ItemServiceHelper itemServiceHelper

    @Autowired
    CharacterServiceHelper characterServiceHelper

    void cleanup() {
        deleteServiceHelper.clean()
    }

    def "should create item"() {
        given:
            def request = ItemHelper.createRequest()
        when:
            def response = httpPost(baseUrl, request, ItemLite, [accessToken: adminAccessToken])
        then:
            response.status == HttpStatus.OK
            ItemHelper.compare(request, response.body)
    }

    def "should not create item"() {
        given:
            itemServiceHelper.saveItem(name: "Item", type: ItemType.WEAPON)
            def request = ItemHelper.createRequest(name: "Item", type: ItemType.WEAPON)
        when:
            def response = httpPost(baseUrl, request, ItemLite, [accessToken: adminAccessToken])
        then:
            response.status == HttpStatus.CONFLICT
            response.errorMessage == ErrorCodes.ITEM_ALREADY_EXISTS.toString()
    }

    def "should find items"() {
        given:
            itemServiceHelper.saveItem(name: "Sword", type: ItemType.WEAPON)
            def item1 = itemServiceHelper.saveItem(name: "Item1", type: ItemType.WEAPON)
            def item2 = itemServiceHelper.saveItem(name: "Item2", type: ItemType.WEAPON)
            itemServiceHelper.saveItem(name: "Item3", type: ItemType.ARMOR)
        and:
            def request = ItemHelper.createItemSearchRequest(nameLike: "Item", typeIn: [ItemType.WEAPON])
        when:
            def response = httpPost(searchUrl, request, ItemLitePage, [accessToken: adminAccessToken])
        then:
            response.status == HttpStatus.OK
            response.body.numberOfElements == 2
            ItemHelper.compare([item1, item2], response.body)
    }

    def "should buy items"() {
        given:
            def character = characterServiceHelper.createCharacter(user, [equipment: Equipment.builder().gold(1000).build()])
            def item1 = itemServiceHelper.saveItem(name: "Item1", type: ItemType.WEAPON, price: 30)
            def item2 = itemServiceHelper.saveItem(name: "Item2", type: ItemType.ARMOR, price: 70)
            def item3 = itemServiceHelper.saveItem(name: "Item3", type: ItemType.WEAPON, price: 100)
        when:
            def response = httpGet(buyItemUrl(item1.id, character.id), ItemLite, [accessToken: userAccessToken])
            character = characterServiceHelper.getCharacter(character.id)
        then:
            response.status == HttpStatus.OK
            ItemHelper.compare(item1, response.body)
            ItemHelper.compare(character.equipment.weapon, item1)
            character.equipment.gold == 970
        when:
            response = httpGet(buyItemUrl(item2.id, character.id), ItemLite, [accessToken: userAccessToken])
            character = characterServiceHelper.getCharacter(character.id)
        then:
            response.status == HttpStatus.OK
            ItemHelper.compare(item2, response.body)
            ItemHelper.compare(character.equipment.weapon, item1)
            ItemHelper.compare(character.equipment.armor, item2)
            character.equipment.gold == 900
        when:
            response = httpGet(buyItemUrl(item3.id, character.id), ItemLite, [accessToken: userAccessToken])
            character = characterServiceHelper.getCharacter(character.id)
        then:
            response.status == HttpStatus.OK
            ItemHelper.compare(item3, response.body)
            ItemHelper.compare(character.equipment.weapon, item3)
            ItemHelper.compare(character.equipment.armor, item2)
            character.equipment.gold == 800
    }

    def "should not buy item"() {
        given:
            def character = characterServiceHelper.createCharacter(user, [equipment: Equipment.builder().gold(99).build()])
            def item1 = itemServiceHelper.saveItem(name: "Item1", type: ItemType.WEAPON, price: 100)
        when:
            def response = httpGet(buyItemUrl(item1.id, character.id), ItemLite, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.FORBIDDEN
            response.errorMessage == ErrorCodes.NOT_ENOUGH_GOLD.toString()
    }

    def "should buy potion"() {
        given:
            def character = characterServiceHelper.createCharacter(user, [equipment: Equipment.builder().gold(1000).healthPotions(0).build()])
        when:
            def response = httpGet(buyPotionUrl(character.id), Void, [accessToken: userAccessToken])
            character = characterServiceHelper.getCharacter(character.id)
        then:
            response.status == HttpStatus.NO_CONTENT
            character.equipment.healthPotions == 1
            character.equipment.gold == 1000 - AbstractItemService.POTION_PRICE
    }

    def "should not buy potion"() {
        given:
            def character = characterServiceHelper.createCharacter(user, [equipment: Equipment.builder().gold(AbstractItemService.POTION_PRICE - 1).healthPotions(0).build()])
        when:
            def response = httpGet(buyPotionUrl(character.id), Void, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.FORBIDDEN
            response.errorMessage == ErrorCodes.NOT_ENOUGH_GOLD.toString()
    }

    def "should get info about potion"() {
        when:
            def response = httpGet(potionInfoUrl, PotionLite, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.OK
            response.body.price == AbstractItemService.POTION_PRICE
            response.body.healPercent == AbstractItemService.POTION_HEAL_PERCENT
    }

    def "should use potion out of fight"() {
        given:
            def character = characterServiceHelper.createCharacter(user, [statistics: StatisticsHelper.statistics(maxHp: 100, currentHp: 50), equipment: Equipment.builder().healthPotions(1).build()])
        when:
            def response = httpGet(usePotionUrl(character.id), StatisticsLite, [accessToken: userAccessToken])
            character = characterServiceHelper.getCharacter(character.id)
        then:
            response.status == HttpStatus.OK
            StatisticsHelper.compare(character.statistics, response.body)
            character.equipment.healthPotions == 0
            character.statistics.currentHp == Math.round(50 + character.statistics.maxHp * AbstractItemService.POTION_HEAL_PERCENT / 100)
    }
}
