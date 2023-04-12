package com.meteo.meteo.Utils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Scanner;
import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import com.meteo.meteo.Models.MeasurementEntity;
import com.meteo.meteo.Models.StateEntity;
import com.meteo.meteo.Repositories.MeasurementRepository;
import com.meteo.meteo.Repositories.StateRepository;

@Service
public class OpenWeatherApi {
    public static String townApiUrl = "https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s";
    public static String measurementApiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s";

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    public StateEntity downloadTown(String town) throws Exception {
        if (town == null || town.isEmpty()) {
            throw new Exception("Invalid input, downloadTown()");
        }

        StateEntity entity = this.stateRepository.findByName(town);
        if (!ObjectUtils.isEmpty(entity)) {
            return entity;
        }

        String data = this.fetch(new URL(String.format(townApiUrl, town, System.getenv("OPENWEATHER_API"))));

        if (data.replace("[", "").replace("]", "").isEmpty()) {
            throw new Exception(String.format("Entered town: %s not found in weather API.", town));
        }

        JSONObject object = new JSONObject(data.replace("[", "").replace("]", ""));

        entity = new StateEntity();
        entity.setName(this.getString(object, "name"));
        entity.setCountry(this.getString(object, "country"));
        entity.setState(this.getString(object, "state"));
        entity.setLatitude(this.getDouble(object, "lat"));
        entity.setLongitude(this.getDouble(object, "lon"));

        this.stateRepository.save(entity);

        return entity;
    }

    public MeasurementEntity downloadMeasurement(StateEntity state) throws Exception {
        if (state == null) {
            throw new Exception("Invalid input, downloadMeasurement()");
        }

        String data = this.fetch(new URL(String.format(measurementApiUrl,
                state.getLatitude(),
                state.getLongitude(),
                System.getenv("OPENWEATHER_API"))));

        JSONObject object = new JSONObject(data);
        MeasurementEntity entity = new MeasurementEntity();

        entity.setState(state);

        JSONObject weather = (JSONObject) ((JSONArray) object.get("weather")).get(0);
        entity.setWeatherMain(this.getString(weather, "main"));
        entity.setWeatherDescription(this.getString(weather, "description"));

        JSONObject main = (JSONObject) object.get("main");
        entity.setTemperature(this.getDouble(main, "temp"));
        entity.setHumidity(this.getInteger(main, "humidity"));
        entity.setPressure(this.getInteger(main, "pressure"));

        JSONObject wind = (JSONObject) object.get("wind");
        entity.setWindSpeed(this.getDouble(wind, "speed"));
        entity.setWindDegree(this.getDouble(wind, "deg"));

        JSONObject clouds = (JSONObject) object.get("clouds");
        entity.setClouds(this.getDouble(clouds, "all"));

        LocalDateTime dateTime = LocalDateTime.now();
        entity.setSaved(dateTime);
        entity.setExpires(dateTime.plusDays(14));

        this.measurementRepository.save(entity);

        return entity;
    }

    private String fetch(URL url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responsecode = conn.getResponseCode();

        if (responsecode != 200) {
            throw new Exception("HttpResponseCode: " + responsecode);
        } else {
            String inline = "";
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                inline += scanner.nextLine();
            }
            scanner.close();
            return inline;
        }
    }

    private String getString(JSONObject object, String key) {
        if (!object.has(key)) {
            return "";
        }
        return object.getString(key);
    }

    private Double getDouble(JSONObject object, String key) {
        if (!object.has(key)) {
            return 0.0;
        }
        return object.getDouble(key);
    }

    private Integer getInteger(JSONObject object, String key) {
        if (!object.has(key)) {
            return 0;
        }
        return object.getInt(key);
    }
}
