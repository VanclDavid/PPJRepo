package com.meteo.meteo.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.meteo.meteo.Models.MeasurementEntity;
import com.meteo.meteo.Repositories.MeasurementRepository;

@Service
public class DBUtils {
    @Autowired
    private MeasurementRepository measurementRepository;

    public List<MeasurementEntity> deleteExpired(String datetime) {
        LocalDateTime expireLimit = LocalDateTime.now();
        if (datetime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            expireLimit = LocalDateTime.parse(datetime, formatter);
        }

        List<MeasurementEntity> expiredData = this.measurementRepository.findExpired(expireLimit);
        expiredData.forEach(entity -> {
            measurementRepository.delete(entity);
        });

        return expiredData;
    }
}
