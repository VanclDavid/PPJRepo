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
    List<MeasurementEntity> findExpired(LocalDateTime dateTime);

    List<MeasurementEntity> findAll();

    @Query(value = "SELECT m.state, AVG(m.clouds) as clouds, AVG(m.humidity) as humidity, AVG(m.pressure) as pressure, AVG(m.temperature) as temperature, AVG(m.wind_degree) as wind_degree, AVG(m.wind_speed) as wind_speed FROM measurements m WHERE m.saved <= NOW() AND m.saved >= NOW() - INTERVAL 1 DAY GROUP BY state", nativeQuery = true)
    List<?> getDayStats();

    @Query(value = "SELECT m.state, AVG(m.clouds) as clouds, AVG(m.humidity) as humidity, AVG(m.pressure) as pressure, AVG(m.temperature) as temperature, AVG(m.wind_degree) as wind_degree, AVG(m.wind_speed) as wind_speed FROM measurements m WHERE m.saved <= NOW() AND m.saved >= NOW() - INTERVAL 7 DAY GROUP BY state", nativeQuery = true)
    List<?> getWeekStats();

    @Query(value = "SELECT m.state, AVG(m.clouds) as clouds, AVG(m.humidity) as humidity, AVG(m.pressure) as pressure, AVG(m.temperature) as temperature, AVG(m.wind_degree) as wind_degree, AVG(m.wind_speed) as wind_speed FROM measurements m WHERE m.saved <= NOW() AND m.saved >= NOW() - INTERVAL 14 DAY GROUP BY state", nativeQuery = true)
    List<?> get14DayStats();
}
