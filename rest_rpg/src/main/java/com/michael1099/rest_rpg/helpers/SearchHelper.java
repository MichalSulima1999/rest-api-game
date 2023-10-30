package com.michael1099.rest_rpg.helpers;

import jakarta.validation.constraints.NotNull;
import org.openapitools.model.Pagination;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class SearchHelper {

    public static Pageable getPageable(@NotNull Pagination pagination) {
        var pageableSort = Sort.by(pagination.getSort());
        pageableSort = pagination.getSortOrder().equalsIgnoreCase("asc") ? pageableSort.ascending() : pageableSort.descending();
        pageableSort = pageableSort.and(Sort.by("id"));
        return PageRequest.of(pagination.getPageNumber(), pagination.getElements(), pageableSort);
    }
}
