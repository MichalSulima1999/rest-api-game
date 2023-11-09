package com.michael1099.rest_rpg.item;

import com.michael1099.rest_rpg.adventure.model.QAdventure;
import com.michael1099.rest_rpg.item.model.Item;
import com.michael1099.rest_rpg.item.model.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;
import org.openapitools.model.ItemSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.QPageRequest;

import static com.michael1099.rest_rpg.helpers.SearchHelper.getQSort;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;
    private final PathBuilderFactory pathBuilderFactory;

    public ItemRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.pathBuilderFactory = new PathBuilderFactory();
    }

    @Override
    public Page<Item> findItems(@NotNull ItemSearchRequest request, @NotNull Pageable pageable) {
        var item = QItem.item;
        var predicate = buildPredicate(item, request);

        var pageRequest = QPageRequest.of(request.getPagination().getPageNumber(), request.getPagination().getElements(), getQSort(request.getPagination()));

        var idQuery = queryFactory.select(item.id)
                .from(item)
                .where(predicate);

        var querydsl = new Querydsl(entityManager, pathBuilderFactory.create(QAdventure.class));
        querydsl.applyPagination(pageRequest, idQuery);

        var query = queryFactory.select(item)
                .from(item)
                .where(item.id.in(idQuery.fetch()));

        querydsl.applySorting(pageRequest.getSort(), query);

        var skills = query.fetch();
        var total = idQuery.fetchCount();

        return new PageImpl<>(skills, pageable, total);
    }

    private BooleanBuilder buildPredicate(@NotNull QItem item, @NotNull ItemSearchRequest request) {
        var predicate = new BooleanBuilder();

        if (request.getNameLike() != null) {
            predicate.and(item.name.contains(request.getNameLike()));
        }
        if (request.getTypeIn() != null) {
            predicate.and(item.type.in(request.getTypeIn()));
        }
        if (request.getPowerGreaterThanOrEqual() != null) {
            predicate.and(item.power.goe(request.getPowerGreaterThanOrEqual()));
        }
        if (request.getPowerLessThanOrEqual() != null) {
            predicate.and(item.power.loe(request.getPowerLessThanOrEqual()));
        }
        if (request.getPriceGreaterThanOrEqual() != null) {
            predicate.and(item.price.goe(request.getPriceGreaterThanOrEqual()));
        }
        if (request.getPriceLessThanOrEqual() != null) {
            predicate.and(item.price.loe(request.getPriceLessThanOrEqual()));
        }

        return predicate;
    }
}
