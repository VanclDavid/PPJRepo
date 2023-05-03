package com.meteo.meteo.Controllers;

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
import com.meteo.meteo.Models.StateEntity;
import com.meteo.meteo.Repositories.StateRepository;
import com.meteo.meteo.Utils.LogUtil;

@Controller
public class ApiJsonController {
    @Autowired
    private LogUtil logger;

    @Autowired
    private StateRepository stateRepository;

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
    public @ResponseBody ResponseEntity<?> getAllStates() {
        return new ResponseEntity<>(stateRepository.findAll(), HttpStatus.OK);
    }

    private ResponseEntity<?> readOnlyModeView() {
        String msg = "Accessing application api in read only mode.";
        this.logger.logWarning(msg);

        JSONObject message = new JSONObject();
        message.put("warning", msg);
        return new ResponseEntity<>(message.toString(), HttpStatus.BAD_REQUEST);
    }

}
