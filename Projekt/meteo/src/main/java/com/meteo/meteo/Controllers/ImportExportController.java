package com.meteo.meteo.Controllers;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.beanutils.PropertyUtils;
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
import com.meteo.meteo.Models.MeasurementEntity;
import com.meteo.meteo.Models.StateEntity;
import com.meteo.meteo.Repositories.MeasurementRepository;
import com.meteo.meteo.Utils.OpenWeatherApi;
import com.opencsv.CSVWriter;

@Controller
public class ImportExportController {
    @Autowired
    private OpenWeatherApi openWeatherApi;

    @Autowired
    private MeasurementRepository measurementRepository;

    @GetMapping("/import-export")
    public String importExportForm(ModelMap modelMap) {
        modelMap.addAttribute("town", "");
        modelMap.addAttribute("file", null);

        return "import-export";
    }

    @PostMapping("/remove-expired")
    public String deleteSubmit(ModelMap modelMap) {

        return "import-export";
    }

    @PostMapping("/download")
    public String importSubmit(@RequestParam("town") String town, ModelMap modelMap) {
        try {
            StateEntity stateEntity = openWeatherApi.downloadTown(town);
            openWeatherApi.downloadMeasurement(stateEntity);
            modelMap.addAttribute("resultState", "success");
            modelMap.addAttribute("resultMessage", "Successfuly downloaded and saved to DB");
        } catch (Exception e) {
            modelMap.addAttribute("resultState", "danger");
            modelMap.addAttribute("resultMessage", e.getMessage());
            // TODO: LOG
        }

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
            List<String[]> csvData = loadMeasurementsFromDB();

            CSVWriter writer = new CSVWriter(new FileWriter("export.csv"));
            writer.writeAll(csvData);
            writer.close();

            File file = new File("export.csv");
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

        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    private List<String[]> loadMeasurementsFromDB() throws Exception {
        List<MeasurementEntity> measurementEntities = this.measurementRepository.findAll();

        List<String[]> list = new ArrayList<>();
        AtomicBoolean isHeaderSet = new AtomicBoolean(false);

        if (!measurementEntities.isEmpty()) {
            measurementEntities.forEach(entity -> {
                if (!isHeaderSet.get()) {
                    ArrayList<String> header = entity.getHeader();
                    list.add(header.toArray(new String[header.size()]));
                    isHeaderSet.set(true);
                }

                ArrayList<String> record = entity.getData();
                list.add(record.toArray(new String[record.size()]));
            });
        }

        return list;
    }
}
