package com.michael1099.rest_rpg.equipment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    default Equipment getEquipmentById(long id) {
        return findById(id).orElseThrow();
    }
}
