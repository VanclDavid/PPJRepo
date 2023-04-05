package com.meteo.meteo.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.meteo.meteo.Models.MeasurementEntity;

@Repository
public interface MeasurementRepository extends CrudRepository<MeasurementEntity, Integer> {
    MeasurementEntity findById(String id);
}
