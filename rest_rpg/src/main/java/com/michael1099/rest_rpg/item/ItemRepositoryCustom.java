package com.michael1099.rest_rpg.item;

import com.michael1099.rest_rpg.item.model.Item;
import jakarta.validation.constraints.NotNull;
import org.openapitools.model.ItemSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepositoryCustom {

    Page<Item> findItems(@NotNull ItemSearchRequest request, @NotNull Pageable pageable);
}
