package com.michael1099.rest_rpg.item

import com.michael1099.rest_rpg.item.model.Item
import org.openapitools.model.ItemType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ItemServiceHelper {

    @Autowired
    ItemRepository itemRepository

    def clean() {
        itemRepository.deleteAll()
    }

    Item saveItem(Map customArgs = [:]) {
        Map args = [
                name : "Sword" + Math.random().toString(),
                type : ItemType.WEAPON,
                power: 30,
                price: 50,
        ]
        args << customArgs

        def item = Item.builder()
                .name(args.name)
                .type(args.type)
                .power(args.power)
                .price(args.price)
                .build()

        return itemRepository.save(item)
    }
}
