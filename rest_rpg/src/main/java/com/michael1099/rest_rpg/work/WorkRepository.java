package com.michael1099.rest_rpg.work;

import com.michael1099.rest_rpg.exceptions.WorkNotFoundException;
import com.michael1099.rest_rpg.work.model.Work;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long>, WorkRepositoryCustom {

    boolean existsByNameIgnoreCase(@NotBlank String name);

    default Work getWorkById(long workId) {
        return findById(workId).orElseThrow(WorkNotFoundException::new);
    }
}
