package com.meteo.meteo.Utils;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.meteo.meteo.Models.MeasurementEntity;
import com.meteo.meteo.Models.StateEntity;
import com.meteo.meteo.Repositories.MeasurementRepository;
import com.meteo.meteo.Utils.ImportExportUtil;
import com.opencsv.CSVWriter;

@Service
public class ImportExportUtil {
    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private OpenWeatherApi openWeatherApi;

    public void importCsv(MultipartFile file, Boolean update) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("File is empty");
        }

        AtomicBoolean isHeaderSet = new AtomicBoolean(false);
        new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
                .lines()
                .forEach(e -> {
                    try {
                        parseDataLine(e, !isHeaderSet.get(), update);
                        isHeaderSet.set(true);
                    } catch (Exception ex) {
                        // TODO: log
                    }
                });
    }

    private void parseDataLine(String row, boolean isHeader, boolean update) throws Exception {
        if (isHeader) {
            return;
        }

        String[] data = row.split(",");
        MeasurementEntity entity = new MeasurementEntity();

        StateEntity state = this.openWeatherApi.downloadTown(this.getString(data, 0));
        entity.setState(state);

        entity.setWeatherMain(this.getString(data, 1));
        entity.setWeatherDescription(this.getString(data, 2));
        entity.setTemperature(this.getDouble(data, 3));
        entity.setHumidity(this.getInteger(data, 4));
        entity.setPressure(this.getInteger(data, 5));
        entity.setWindSpeed(this.getDouble(data, 6));
        entity.setWindDegree(this.getDouble(data, 7));
        entity.setClouds(this.getDouble(data, 8));

        if (update) {
            LocalDateTime dateTime = LocalDateTime.now();
            entity.setSaved(dateTime);
            entity.setExpires(dateTime.plusDays(Long.parseLong(System.getenv("EXPIRATION_IN_DAYS"))));
        } else {
            entity.setSaved(LocalDateTime.parse(this.getString(data, 9)));
            entity.setExpires(LocalDateTime.parse(this.getString(data, 10)));
        }

        this.measurementRepository.save(entity);
    }

    public ResponseEntity<Resource> exportCsv() throws Exception {
        List<String[]> csvData = this.loadMeasurementsFromDB();

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

        ResponseEntity<Resource> response = ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);

        file.delete();
        return response;
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

    private String getString(String[] data, int index) {
        return data[index].replace("\"", "");
    }

    private Double getDouble(String[] data, int index) {
        return Double.parseDouble(data[index].replace("\"", ""));
    }

    private Integer getInteger(String[] data, int index) {
        return Integer.parseInt(data[index].replace("\"", ""));
    }
}
