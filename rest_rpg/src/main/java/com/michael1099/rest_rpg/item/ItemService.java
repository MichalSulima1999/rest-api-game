package com.michael1099.rest_rpg.item;

import jakarta.validation.constraints.NotNull;
import org.openapitools.model.ItemCreateRequest;
import org.openapitools.model.ItemLite;
import org.openapitools.model.ItemLitePage;
import org.openapitools.model.ItemSearchRequest;
import org.openapitools.model.PotionLite;
import org.openapitools.model.StatisticsLite;


public interface ItemService {

    ItemLite createItem(@NotNull ItemCreateRequest request);

    ItemLitePage findItems(@NotNull ItemSearchRequest request);

    ItemLite buyItem(long itemId, long characterId);

    void buyPotion(long characterId);

    StatisticsLite usePotion(long characterId);

    PotionLite getPotionInfo();
}
