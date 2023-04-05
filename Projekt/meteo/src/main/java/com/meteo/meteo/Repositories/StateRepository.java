package com.meteo.meteo.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.meteo.meteo.Models.StateEntity;

@Repository
public interface StateRepository extends CrudRepository<StateEntity, Integer> {
    StateEntity findByName(String name);
}
