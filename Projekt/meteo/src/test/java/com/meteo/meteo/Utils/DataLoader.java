package com.meteo.meteo.Utils;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.meteo.meteo.Models.MeasurementEntity;
import com.meteo.meteo.Models.StateEntity;
import com.meteo.meteo.Repositories.MeasurementRepository;
import com.meteo.meteo.Repositories.StateRepository;

@Service
public class DataLoader extends JsonHelper {
    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private OpenWeatherApi openWeatherApi;

    public void loadData(String filename) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("states.json").getFile());

            String content = new String(Files.readAllBytes(file.toPath()));
            JSONObject data = new JSONObject(content);

            JSONArray states = (JSONArray) data.get("states");
            for (Object object : states) {
                if (object instanceof JSONObject) {
                    JSONObject row = (JSONObject) object;

                    StateEntity entity = new StateEntity();
                    entity.setCountry(this.getString(row, "country"));
                    entity.setName(this.getString(row, "name"));
                    entity.setState(this.getString(row, "state"));
                    entity.setLatitude(this.getDouble(row, "latitude"));
                    entity.setLongitude(this.getDouble(row, "longitude"));

                    this.stateRepository.save(entity);
                }
            }

            JSONArray measurements = (JSONArray) data.get("measurements");
            for (Object object : measurements) {
                if (object instanceof JSONObject) {
                    JSONObject row = (JSONObject) object;

                    MeasurementEntity entity = new MeasurementEntity();

                    StateEntity state = this.openWeatherApi.downloadTown(this.getString(row, "stateName"));
                    entity.setState(state);
                    entity.setWeatherMain(this.getString(row, "weatherMain"));
                    entity.setWeatherDescription(this.getString(row, "weatherDescription"));

                    entity.setTemperature(this.getDouble(row, "temperature"));
                    entity.setHumidity(this.getInteger(row, "humidity"));
                    entity.setPressure(this.getInteger(row, "pressure"));

                    entity.setWindSpeed(this.getDouble(row, "windSpeed"));
                    entity.setWindDegree(this.getDouble(row, "windDegree"));

                    entity.setClouds(this.getDouble(row, "clouds"));

                    entity.setSaved(LocalDateTime.parse(this.getString(row, "saved")));
                    entity.setExpires(LocalDateTime.parse(this.getString(row, "expires")));

                    this.measurementRepository.save(entity);
                }
            }

        } catch (Exception e) {

        }
    }

    public void setStateRepository(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }
}
