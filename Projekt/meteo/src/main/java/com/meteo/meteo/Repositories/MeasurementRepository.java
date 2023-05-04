package com.meteo.meteo.Repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.meteo.meteo.Models.MeasurementEntity;
import com.meteo.meteo.Models.MeasurementId;

@Repository
public interface MeasurementRepository extends JpaRepository<MeasurementEntity, MeasurementId> {
    List<MeasurementEntity> findByIdSaved(LocalDateTime saved);

    List<MeasurementEntity> findByIdState(String state);

    @Query(value = "SELECT * FROM measurements m WHERE m.saved = ?1 AND m.state = ?2", nativeQuery = true)
    MeasurementEntity findById(LocalDateTime saved, String state);

    @Query(value = "SELECT * FROM measurements m WHERE m.expires < ?1 AND m.expires IS NOT NULL", nativeQuery = true)
    public List<MeasurementEntity> findExpired(LocalDateTime dateTime);

    List<MeasurementEntity> findAll();
}
