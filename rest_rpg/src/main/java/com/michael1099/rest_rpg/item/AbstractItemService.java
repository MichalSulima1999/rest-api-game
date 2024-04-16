package com.michael1099.rest_rpg.item;

import com.michael1099.rest_rpg.auth.auth.IAuthenticationFacade;
import com.michael1099.rest_rpg.character.CharacterRepository;
import com.michael1099.rest_rpg.statistics.StatisticsMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractItemService implements ItemService {

    public static final int POTION_PRICE = 50;
    public static final int POTION_HEAL_PERCENT = 30;

    protected final ItemRepository itemRepository;
    protected final CharacterRepository characterRepository;
    protected final IAuthenticationFacade authenticationFacade;
    protected final ItemMapper itemMapper;
    protected final StatisticsMapper statisticsMapper;
}
