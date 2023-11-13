package com.michael1099.rest_rpg.work;

import com.michael1099.rest_rpg.work.model.Work;
import jakarta.validation.constraints.NotNull;
import org.openapitools.model.WorkSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepositoryCustom {

    Page<Work> findWorks(@NotNull WorkSearchRequest request, @NotNull Pageable pageable);
}
