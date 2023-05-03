package com.meteo.meteo.Repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.meteo.meteo.Models.MeasurementEntity;

@Repository
public interface MeasurementRepository extends JpaRepository<MeasurementEntity, Integer> {
    MeasurementEntity findBySaved(LocalDateTime saved);

    @Query(value = "SELECT * FROM measurements m WHERE m.expires < ?1 AND m.expires IS NOT NULL", nativeQuery = true)
    public List<MeasurementEntity> findExpired(LocalDateTime dateTime);

    List<MeasurementEntity> findAll();
}
