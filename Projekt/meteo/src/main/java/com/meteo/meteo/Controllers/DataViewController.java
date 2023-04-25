package com.meteo.meteo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.meteo.meteo.Repositories.MeasurementRepository;
import com.meteo.meteo.Repositories.StateRepository;

@Controller
public class DataViewController {
    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @GetMapping("/data-view")
    public String importExportForm(ModelMap modelMap) {
        return "data-view";
    }

    @RequestMapping(value = "/list-states", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> getAllStates() {
        return new ResponseEntity<>(stateRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/list-measurements", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> getAllMeasurements() {
        return new ResponseEntity<>(measurementRepository.findAll(), HttpStatus.OK);
    }
}
