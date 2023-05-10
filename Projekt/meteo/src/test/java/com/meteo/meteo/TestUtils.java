package com.meteo.meteo;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import org.aspectj.util.FileUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mock.web.MockMultipartFile;

import com.meteo.meteo.Models.MeasurementEntity;
import com.meteo.meteo.Models.StateEntity;
import com.meteo.meteo.Repositories.MeasurementRepository;
import com.meteo.meteo.Repositories.StateRepository;
import com.meteo.meteo.Utils.DBUtils;
import com.meteo.meteo.Utils.DataLoader;
import com.meteo.meteo.Utils.ImportExportUtil;
import com.meteo.meteo.Utils.OpenWeatherApi;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@ComponentScan("com.meteo.meteo.Utils")
public class TestUtils {
    @Autowired
    private DBUtils dbUtils;

    @Autowired
    private ImportExportUtil importExportUtil;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private OpenWeatherApi openWeatherApi;

    @Autowired
    private DataLoader dataLoader;

    @org.junit.Before
    public void setUpData() {
        this.dataLoader.loadData("data_set");
    }

    @Test
    public void deleteExpiredTest() {
        List<MeasurementEntity> measurements = this.measurementRepository.findAll();
        Assert.assertEquals(5, measurements.size());
        this.dbUtils.deleteExpired("2023-05-16 20:10:45");

        measurements = this.measurementRepository.findAll();
        Assert.assertEquals(1, measurements.size());
    }

    @Test
    public void exportCSVDataTest() {
        int lines = 0;

        try {
            InputStreamResource exportedFile = this.importExportUtil.fileCsvData();
            String data = exportedFile.getContentAsString(Charset.forName("UTF-8"));
            String[] multiLines = data.split("\r\n|\r|\n");

            String header = "\"state\",\"weather_main\",\"weather_description\",\"temperature\",\"humidity\",\"pressure\",\"wind_speed\",\"wind_degree\",\"clouds\",\"saved\",\"expires\"";
            String firstRow = "\"Berlin\",\"Clouds\",\"broken clouds\",\"283.6\",\"57\",\"1022\",\"3.13\",\"293.0\",\"75.0\",\"2023-05-02T19:10:40.750193\",\"2023-05-16T19:10:40.750193\"";
            Assert.assertEquals(header, multiLines[0].toString());
            Assert.assertEquals(firstRow, multiLines[1].toString());

            lines = multiLines.length;
        } catch (Exception e) {
            Assert.assertNotNull(null);
        }

        Assert.assertEquals(6, lines);
    }

    @Test
    public void importCSVDataTest() {
        List<MeasurementEntity> measurements = this.measurementRepository.findAll();
        Assert.assertEquals(5, measurements.size());

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("import_set.csv").getFile());

            String name = "import_set.csv";
            String originalFileName = "import_set.csv";
            String contentType = "text/plain";
            byte[] content = FileUtil.readAsByteArray(file);
            MultipartFile multipartFile = new MockMultipartFile(name, originalFileName, contentType, content);

            this.importExportUtil.importCsv(multipartFile, true);
        } catch (Exception e) {
            Assert.assertNotNull(null);
        }

        measurements = this.measurementRepository.findAll();
        Assert.assertEquals(19, measurements.size());
    }

    @Test
    public void saveTownAndMeasurementAsJSON() {
        StateEntity entity = this.openWeatherApi.saveDownloadedTownFromJSON(loadStateJsonAsString());
        this.openWeatherApi.saveDownloadedMeasurementFromJSON(loadMeasurementJsonAsString(), entity);

        StateEntity dbTown = this.stateRepository.findByName("Ostrava");
        Assert.assertEquals((Double) 49.8349139, dbTown.getLatitude());
        Assert.assertEquals((Double) 18.2820084, dbTown.getLongitude());

        List<MeasurementEntity> dbMeasurements = this.measurementRepository.findByIdState("Ostrava");
        Assert.assertEquals(1, dbMeasurements.size());
        Assert.assertEquals("clear sky", dbMeasurements.get(0).getWeatherDescription());
    }

    private String loadMeasurementJsonAsString() {
        return "{\"coord\":{\"lon\":14.424,\"lat\":50.0893},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"base\":\"stations\",\"main\":{\"temp\":287.31,\"feels_like\":286.35,\"temp_min\":286.18,\"temp_max\":288.49,\"pressure\":1009,\"humidity\":60},\"visibility\":10000,\"wind\":{\"speed\":4.02,\"deg\":90},\"clouds\":{\"all\":0},\"dt\":1683703132,\"sys\":{\"type\":2,\"id\":2010430,\"country\":\"CZ\",\"sunrise\":1683689046,\"sunset\":1683743604},\"timezone\":7200,\"id\":3065328,\"name\":\"Old Town\",\"cod\":200}";
    }

    private String loadStateJsonAsString() {
        return "[{\"name\":\"Ostrava\",\"local_names\":{\"bg\":\"Острава\",\"el\":\"Οστράβα\",\"de\":\"Ostrau\",\"pl\":\"Ostrawa\",\"la\":\"Ostravia\",\"ko\":\"오스트라바\",\"ar\":\"أوسترافا\",\"ur\":\"اوستراوا\",\"ps\":\"اوستراوا\",\"zh\":\"俄斯特拉发\",\"he\":\"אוסטרבה\",\"sr\":\"Острава\",\"ka\":\"ოსტრავა\",\"uk\":\"Острава\",\"ce\":\"Острава\",\"ja\":\"オストラヴァ\",\"mr\":\"ओस्त्राव्हा\",\"th\":\"ออสตราวา\",\"ru\":\"Острава\",\"tg\":\"Острава\",\"kk\":\"Острава\",\"fa\":\"استراوا\",\"cs\":\"Ostrava\",\"mk\":\"Острава\",\"be\":\"Острава\"},\"lat\":49.8349139,\"lon\":18.2820084,\"country\":\"CZ\",\"state\":\"Moravia-Silesia\"}]";
    }
}
