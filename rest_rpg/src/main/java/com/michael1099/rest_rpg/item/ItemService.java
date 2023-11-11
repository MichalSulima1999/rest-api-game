package com.michael1099.rest_rpg.item;

import com.michael1099.rest_rpg.auth.auth.IAuthenticationFacade;
import com.michael1099.rest_rpg.character.CharacterRepository;
import com.michael1099.rest_rpg.exceptions.ItemAlreadyExistsException;
import com.michael1099.rest_rpg.exceptions.NotEnoughGoldException;
import com.michael1099.rest_rpg.helpers.SearchHelper;
import com.michael1099.rest_rpg.item.model.Item;
import com.michael1099.rest_rpg.item.model.ItemCreateRequestDto;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.ItemCreateRequest;
import org.openapitools.model.ItemLite;
import org.openapitools.model.ItemLitePage;
import org.openapitools.model.ItemSearchRequest;
import org.openapitools.model.PotionLite;
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

        var item = itemRepository.getItemById(itemId);
        if (item.getPrice() > character.getEquipment().getGold()) {
            throw new NotEnoughGoldException();
        }
        character.buyItem(item);
        characterRepository.save(character);
        return itemMapper.toLite(item);
    }

    @Transactional
    public void buyPotion(long characterId) {
        var character = characterRepository.getCharacterById(characterId);
        authenticationFacade.checkIfCharacterBelongsToUser(character);

        if (POTION_PRICE > character.getEquipment().getGold()) {
            throw new NotEnoughGoldException();
        }
        character.buyPotion();
        characterRepository.save(character);
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
}
