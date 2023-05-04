package com.meteo.meteo.Controllers;

import java.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.meteo.meteo.Models.MeasurementEntity;
import com.meteo.meteo.Models.StateEntity;
import com.meteo.meteo.Repositories.MeasurementRepository;
import com.meteo.meteo.Repositories.StateRepository;
import com.meteo.meteo.Utils.LogUtil;

@Controller
public class ApiJsonController {
    @Autowired
    private LogUtil logger;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Value("${spring.mode.read-only}")
    private Boolean readOnlyMode;

    @RequestMapping(value = "/api/addTown", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> addTown(
            @RequestParam(name = "town", required = true) String town,
            @RequestParam(name = "country", required = true) String country,
            @RequestParam(name = "state", required = true) String state,
            @RequestParam(name = "latitude", required = true) Double latitude,
            @RequestParam(name = "longitude", required = true) Double longitude) {

        if (this.readOnlyMode) {
            return this.readOnlyModeView();
        }

        StateEntity entity = this.stateRepository.findByName(town);
        if (!ObjectUtils.isEmpty(entity)) {
            JSONObject message = new JSONObject();
            message.put("message", "This city is already saved");
            return new ResponseEntity<>(message.toString(), HttpStatus.BAD_REQUEST);
        }

        entity = new StateEntity();
        entity.setCountry(country);
        entity.setName(town);
        entity.setState(state);
        entity.setLatitude(latitude);
        entity.setLongitude(longitude);
        this.stateRepository.save(entity);
        this.logger.log(String.format("City %s has been added", town));

        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/editTown", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> editTown(
            @RequestParam(name = "town", required = true) String town,
            @RequestParam(name = "country", required = false) String country,
            @RequestParam(name = "state", required = false) String state,
            @RequestParam(name = "latitude", required = false) Double latitude,
            @RequestParam(name = "longitude", required = false) Double longitude) {

        if (this.readOnlyMode) {
            return this.readOnlyModeView();
        }

        StateEntity entity = this.stateRepository.findByName(town);
        if (ObjectUtils.isEmpty(entity)) {
            JSONObject message = new JSONObject();
            message.put("message", String.format("There is no town named %s", town));
            return new ResponseEntity<>(message.toString(), HttpStatus.BAD_REQUEST);
        }

        if (country != null && !country.isEmpty()) {
            entity.setCountry(country);
        }

        if (town != null && !town.isEmpty()) {
            entity.setName(town);
        }

        if (state != null && !state.isEmpty()) {
            entity.setState(state);
        }

        if (latitude != null && !latitude.isNaN()) {
            entity.setLatitude(latitude);
        }

        if (longitude != null && !longitude.isNaN()) {
            entity.setLongitude(longitude);
        }

        this.stateRepository.save(entity);
        this.logger.log(String.format("City %s has been edited", town));

        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/deleteTown", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> deleteTown(
            @RequestParam(name = "town", required = true) String town) {

        if (this.readOnlyMode) {
            return this.readOnlyModeView();
        }

        StateEntity entity = this.stateRepository.findByName(town);
        if (ObjectUtils.isEmpty(entity)) {
            JSONObject message = new JSONObject();
            message.put("message", String.format("There is no town named %s", town));
            return new ResponseEntity<>(message.toString(), HttpStatus.BAD_REQUEST);
        }

        this.stateRepository.delete(entity);
        this.logger.log(String.format("City %s has been delted", town));

        JSONObject message = new JSONObject();
        message.put("message", String.format("City %s has been delted", town));
        return new ResponseEntity<>(message.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/listTown", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> listTown() {
        return new ResponseEntity<>(stateRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/addMeasurement", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> addMeasurement(
            @RequestParam(name = "town", required = true) String town,
            @RequestParam(name = "weather", required = true) String weather,
            @RequestParam(name = "description", required = true) String description,
            @RequestParam(name = "temperature", required = true) Double temperature,
            @RequestParam(name = "humidity", required = true) Integer humidity,
            @RequestParam(name = "pressure", required = true) Integer pressure,
            @RequestParam(name = "windSpeed", required = true) Double windSpeed,
            @RequestParam(name = "windDegree", required = true) Double windDegree,
            @RequestParam(name = "clouds", required = true) Double clouds) {
        if (this.readOnlyMode) {
            return this.readOnlyModeView();
        }

        StateEntity stateEntity = this.stateRepository.findByName(town);
        if (ObjectUtils.isEmpty(stateEntity)) {
            JSONObject message = new JSONObject();
            message.put("message", String.format("There is no town named %s", town));
            return new ResponseEntity<>(message.toString(), HttpStatus.BAD_REQUEST);
        }

        MeasurementEntity entity = new MeasurementEntity();
        entity.setState(stateEntity);
        entity.setWeatherMain(weather);
        entity.setWeatherDescription(description);
        entity.setTemperature(temperature);
        entity.setHumidity(humidity);
        entity.setPressure(pressure);
        entity.setWindSpeed(windSpeed);
        entity.setWindDegree(windDegree);
        entity.setClouds(clouds);

        LocalDateTime dt = LocalDateTime.now();
        entity.setSaved(dt);
        entity.setExpires(dt.plusDays(Long.parseLong(System.getenv("EXPIRATION_IN_DAYS"))));
        this.measurementRepository.save(entity);
        this.logger.log(String.format("Measurement (%s, %s) has been added", town, dt.toString()));

        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/listMeasurement", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> listMeasurement() {
        return new ResponseEntity<>(measurementRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/editMeasurement", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> editMeasurement(
            @RequestParam(name = "town", required = true) String town,
            @RequestParam(name = "dateTime", required = true) String dateTime,
            @RequestParam(name = "weather", required = false) String weather,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "temperature", required = false) Double temperature,
            @RequestParam(name = "humidity", required = false) Integer humidity,
            @RequestParam(name = "pressure", required = false) Integer pressure,
            @RequestParam(name = "windSpeed", required = false) Double windSpeed,
            @RequestParam(name = "windDegree", required = false) Double windDegree,
            @RequestParam(name = "clouds", required = false) Double clouds) {
        if (this.readOnlyMode) {
            return this.readOnlyModeView();
        }

        MeasurementEntity measurementEntity = this.measurementRepository.findById(LocalDateTime.parse(dateTime), town);
        if (ObjectUtils.isEmpty(measurementEntity)) {
            JSONObject message = new JSONObject();
            message.put("message", String.format("There measurement for town %s at %s", town, dateTime));
            return new ResponseEntity<>(message.toString(), HttpStatus.BAD_REQUEST);
        }

        if (weather != null && !weather.isEmpty()) {
            measurementEntity.setWeatherMain(weather);
        }

        if (temperature != null) {
            measurementEntity.setTemperature(temperature);
        }

        if (description != null && !description.isEmpty()) {
            measurementEntity.setWeatherDescription(description);
        }

        if (humidity != null) {
            measurementEntity.setHumidity(humidity);
        }

        if (pressure != null) {
            measurementEntity.setPressure(pressure);
        }

        if (windDegree != null) {
            measurementEntity.setWindDegree(windDegree);
        }

        if (clouds != null) {
            measurementEntity.setClouds(clouds);
        }

        measurementEntity.setExpires(LocalDateTime.now().plusDays(Long.parseLong(System.getenv("EXPIRATION_IN_DAYS"))));
        this.measurementRepository.save(measurementEntity);
        this.logger.log(String.format("Measurement (%s, %s) has been edited", town, dateTime));

        return new ResponseEntity<>(measurementEntity, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/deleteMeasurement", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> deleteMeasurement(
            @RequestParam(name = "town", required = true) String town,
            @RequestParam(name = "dateTime", required = true) String dateTime) {

        if (this.readOnlyMode) {
            return this.readOnlyModeView();
        }

        MeasurementEntity measurementEntity = this.measurementRepository.findById(LocalDateTime.parse(dateTime), town);
        if (ObjectUtils.isEmpty(measurementEntity)) {
            JSONObject message = new JSONObject();
            message.put("message", String.format("There measurement for town %s at %s", town, dateTime));
            return new ResponseEntity<>(message.toString(), HttpStatus.BAD_REQUEST);
        }

        this.measurementRepository.delete(measurementEntity);
        this.logger.log(String.format("Measurement (%s, %s) has been delted", town, dateTime));

        JSONObject message = new JSONObject();
        message.put("message", String.format("Measurement (%s, %s) has been delted", town, dateTime));
        return new ResponseEntity<>(message.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/statsMeasurement", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> statsMeasurement() {
        JSONObject message = new JSONObject();
        message.put("daily_stats", new JSONArray(measurementRepository.getDayStats()));
        message.put("weekly_stats", new JSONArray(measurementRepository.getDayStats()));
        message.put("fourteenDaysJson", new JSONArray(measurementRepository.getDayStats()));
        return new ResponseEntity<>(message.toString(), HttpStatus.OK);
    }

    private ResponseEntity<?> readOnlyModeView() {
        String msg = "Accessing application api in read only mode.";
        this.logger.logWarning(msg);

        JSONObject message = new JSONObject();
        message.put("warning", msg);
        return new ResponseEntity<>(message.toString(), HttpStatus.BAD_REQUEST);
    }
}
