package com.meteo.meteo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.meteo.meteo.Models.StateEntity;

@Repository
public interface StateRepository extends JpaRepository<StateEntity, Integer> {
    StateEntity findByName(String name);

    List<StateEntity> findAll();
}
