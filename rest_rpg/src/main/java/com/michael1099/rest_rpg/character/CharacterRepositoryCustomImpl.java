package com.michael1099.rest_rpg.character;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.character.model.QCharacter;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class CharacterRepositoryCustomImpl implements CharacterRepositoryCustom {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public CharacterRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Character getWithEntityGraph(long id, String graph) {
        QCharacter character = QCharacter.character;
        return queryFactory.select(character)
                .from(character)
                .where(character.id.eq(id))
                .setHint("javax.persistence.fetchgraph", entityManager.getEntityGraph(graph))
                .fetchOne();
    }
}
