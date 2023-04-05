package com.meteo.meteo.Controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.meteo.meteo.Repositories.MeasurementRepository;
import com.meteo.meteo.Repositories.StateRepository;
import com.meteo.meteo.Utils.OpenWeatherApi;

@Controller
public class ImportExportController {
    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @GetMapping("/import-export")
    public String importExportForm(ModelMap modelMap) {
        modelMap.addAttribute("town", "");
        modelMap.addAttribute("file", null);

        return "import-export";
    }

    @PostMapping("/download")
    public String importSubmit(@RequestParam("town") String town, ModelMap modelMap) {
        OpenWeatherApi weatherApi = new OpenWeatherApi();
        weatherApi.setMeasurementRepository(measurementRepository);
        weatherApi.setStateRepository(stateRepository);

        weatherApi.download(town);
        modelMap.addAttribute("resusltState", "OK");

        return "import-export";
    }

    @PostMapping("/import")
    public String importSubmit(@RequestParam("file") MultipartFile file, ModelMap modelMap) {
        modelMap.addAttribute("file", file);

        System.out.println(file.getSize());

        return "import-export";
    }

    @PostMapping("/export")
    public ResponseEntity<Resource> exportSubmit() {
        try {
            // TODO: load from DB
            File file = new File("filename.txt");
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.csv");
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }
}
