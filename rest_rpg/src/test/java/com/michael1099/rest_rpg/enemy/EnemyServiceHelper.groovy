package com.michael1099.rest_rpg.enemy

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EnemyServiceHelper {

    @Autowired
    EnemyRepository enemyRepository

    def clean() {
        enemyRepository.deleteAll()
    }
}
