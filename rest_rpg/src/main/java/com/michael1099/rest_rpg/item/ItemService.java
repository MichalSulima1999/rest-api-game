package com.michael1099.rest_rpg.item;

import com.michael1099.rest_rpg.auth.auth.IAuthenticationFacade;
import com.michael1099.rest_rpg.character.CharacterRepository;
import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.equipment.Equipment;
import com.michael1099.rest_rpg.exceptions.CharacterHpFullException;
import com.michael1099.rest_rpg.exceptions.CharacterIsOccupiedException;
import com.michael1099.rest_rpg.exceptions.ItemAlreadyBoughtException;
import com.michael1099.rest_rpg.exceptions.ItemAlreadyExistsException;
import com.michael1099.rest_rpg.exceptions.NoPotionsLeftException;
import com.michael1099.rest_rpg.exceptions.NotEnoughGoldException;
import com.michael1099.rest_rpg.helpers.SearchHelper;
import com.michael1099.rest_rpg.item.model.Item;
import com.michael1099.rest_rpg.item.model.ItemCreateRequestDto;
import com.michael1099.rest_rpg.statistics.Statistics;
import com.michael1099.rest_rpg.statistics.StatisticsMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.ItemCreateRequest;
import org.openapitools.model.ItemLite;
import org.openapitools.model.ItemLitePage;
import org.openapitools.model.ItemSearchRequest;
import org.openapitools.model.PotionLite;
import org.openapitools.model.StatisticsLite;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class ItemService {

    public static final int POTION_PRICE = 50;
    public static final int POTION_HEAL_PERCENT = 30;

    private final ItemRepository itemRepository;
    private final CharacterRepository characterRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final ItemMapper itemMapper;
    private final StatisticsMapper statisticsMapper;

    @Transactional
    public ItemLite createItem(@NotNull ItemCreateRequest request) {
        var dto = itemMapper.toDto(request);
        checkIfItemExists(dto);
        var item = Item.of(dto);
        return itemMapper.toLite(itemRepository.save(item));
    }

    @Transactional
    public ItemLitePage findItems(@NotNull ItemSearchRequest request) {
        var pageable = SearchHelper.getPageable(request.getPagination());
        return itemMapper.toPage(itemRepository.findItems(request, pageable));
    }

    @Transactional
    public ItemLite buyItem(long itemId, long characterId) {
        var character = characterRepository.getCharacterById(characterId);
        authenticationFacade.checkIfCharacterBelongsToUser(character);
        checkIfCharacterOccupied(character);

        var item = itemRepository.getItemById(itemId);
        checkIfEnoughGold(item.getPrice(), character.getEquipment().getGold());
        if (character.getEquipment().getArmor() != null && item.getId().equals(character.getEquipment().getArmor().getId()) ||
                character.getEquipment().getWeapon() != null && item.getId().equals(character.getEquipment().getWeapon().getId())) {
            throw new ItemAlreadyBoughtException();
        }
        character.buyItem(item);
        characterRepository.save(character);
        return itemMapper.toLite(item);
    }

    @Transactional
    public void buyPotion(long characterId) {
        var character = characterRepository.getCharacterById(characterId);
        authenticationFacade.checkIfCharacterBelongsToUser(character);
        checkIfEnoughGold(POTION_PRICE, character.getEquipment().getGold());
        checkIfCharacterOccupied(character);
        character.buyPotion();
        characterRepository.save(character);
    }

    @Transactional
    public StatisticsLite usePotion(long characterId) {
        var character = characterRepository.getCharacterById(characterId);
        authenticationFacade.checkIfCharacterBelongsToUser(character);
        checkIfCharacterOccupied(character);
        checkIfHpAlreadyFull(character.getStatistics());
        checkIfCharacterHasPotions(character.getEquipment());
        character.usePotion();
        characterRepository.save(character);
        return statisticsMapper.toLite(character.getStatistics());
    }

    @Transactional
    public PotionLite getPotionInfo() {
        return new PotionLite(POTION_HEAL_PERCENT, POTION_PRICE);
    }

    private void checkIfItemExists(@NotNull ItemCreateRequestDto requestDto) {
        if (itemRepository.existsByNameAndTypeAllIgnoreCase(requestDto.getName(), requestDto.getType())) {
            throw new ItemAlreadyExistsException();
        }
    }

    private void checkIfCharacterOccupied(@NotNull Character character) {
        if (character.getOccupation().isOccupied()) {
            throw new CharacterIsOccupiedException();
        }
    }

    private void checkIfEnoughGold(int price, int characterGold) {
        if (price > characterGold) {
            throw new NotEnoughGoldException();
        }
    }

    private void checkIfHpAlreadyFull(@NotNull Statistics statistics) {
        if (statistics.getCurrentHp() >= statistics.getMaxHp()) {
            throw new CharacterHpFullException();
        }
    }

    private void checkIfCharacterHasPotions(@NotNull Equipment equipment) {
        if (equipment.getHealthPotions() <= 0) {
            throw new NoPotionsLeftException();
        }
    }
}
