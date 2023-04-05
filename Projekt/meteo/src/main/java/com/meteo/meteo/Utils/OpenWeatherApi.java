package com.meteo.meteo.Utils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.*;
import com.meteo.meteo.Models.StateEntity;
import com.meteo.meteo.Repositories.MeasurementRepository;
import com.meteo.meteo.Repositories.StateRepository;

public class OpenWeatherApi {
    private MeasurementRepository measurementRepository;
    private StateRepository stateRepository;

    public OpenWeatherApi() {
    }

    public void download(String town) {
        try {

            URL url = new URL(String.format(
                    "https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=84637b38fdc2b4e18c35c5a8e52fc5df",
                    town));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // Getting the response code
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                String inline = "";
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }
                scanner.close();

                JSONObject object = new JSONObject(inline.replace("[", "").replace("]", ""));

                StateEntity entity = new StateEntity();
                entity.setName(object.getString("name"));
                entity.setCountry(object.getString("country"));
                entity.setState(object.getString("state"));
                entity.setLatitude(object.getDouble("lat"));
                entity.setLongitude(object.getDouble("lon"));

                this.stateRepository.save(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStateRepository(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    public void setMeasurementRepository(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }
}
