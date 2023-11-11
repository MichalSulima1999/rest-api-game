package com.michael1099.rest_rpg.item;

import com.michael1099.rest_rpg.exceptions.ItemNotFoundException;
import com.michael1099.rest_rpg.item.model.Item;
import org.openapitools.model.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    boolean existsByNameAndTypeAllIgnoreCase(String name, ItemType type);

    default Item getItemById(long itemId) {
        return findById(itemId).orElseThrow(ItemNotFoundException::new);
    }
}
