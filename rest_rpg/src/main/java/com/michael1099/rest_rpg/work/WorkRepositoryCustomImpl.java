package com.michael1099.rest_rpg.work;

import com.michael1099.rest_rpg.adventure.model.QAdventure;
import com.michael1099.rest_rpg.work.model.QWork;
import com.michael1099.rest_rpg.work.model.Work;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;
import org.openapitools.model.WorkSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.QPageRequest;

import static com.michael1099.rest_rpg.helpers.SearchHelper.getQSort;

public class WorkRepositoryCustomImpl implements WorkRepositoryCustom {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;
    private final PathBuilderFactory pathBuilderFactory;

    public WorkRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.pathBuilderFactory = new PathBuilderFactory();
    }

    @Override
    public Page<Work> findWorks(WorkSearchRequest request, Pageable pageable) {
        var work = QWork.work;
        var predicate = buildPredicate(work, request);

        var pageRequest = QPageRequest.of(request.getPagination().getPageNumber(), request.getPagination().getElements(), getQSort(request.getPagination()));

        var idQuery = queryFactory.select(work.id)
                .from(work)
                .where(predicate);

        var querydsl = new Querydsl(entityManager, pathBuilderFactory.create(QAdventure.class));
        querydsl.applyPagination(pageRequest, idQuery);

        var query = queryFactory.select(work)
                .from(work)
                .where(work.id.in(idQuery.fetch()));

        querydsl.applySorting(pageRequest.getSort(), query);

        var skills = query.fetch();
        var total = idQuery.fetchCount();

        return new PageImpl<>(skills, pageable, total);
    }

    private BooleanBuilder buildPredicate(@NotNull QWork work, @NotNull WorkSearchRequest request) {
        var predicate = new BooleanBuilder();

        if (request.getNameLike() != null) {
            predicate.and(work.name.contains(request.getNameLike()));
        }
        if (request.getResourceAmountGreaterThanOrEqual() != null) {
            predicate.and(work.resourceAmount.goe(request.getResourceAmountGreaterThanOrEqual()));
        }
        if (request.getResourceAmountLessThanOrEqual() != null) {
            predicate.and(work.resourceAmount.loe(request.getResourceAmountLessThanOrEqual()));
        }
        if (request.getResourceTypeIn() != null) {
            predicate.and(work.resourceType.in(request.getResourceTypeIn()));
        }
        if (request.getWorkMinutesGreaterThanOrEqual() != null) {
            predicate.and(work.workMinutes.goe(request.getWorkMinutesGreaterThanOrEqual()));
        }
        if (request.getWorkMinutesLessThanOrEqual() != null) {
            predicate.and(work.workMinutes.loe(request.getWorkMinutesLessThanOrEqual()));
        }

        return predicate;
    }
}
