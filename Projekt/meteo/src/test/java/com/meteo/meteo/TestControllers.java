package com.meteo.meteo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.meteo.meteo.Models.MeasurementEntity;
import com.meteo.meteo.Models.StateEntity;
import com.meteo.meteo.Repositories.MeasurementRepository;
import com.meteo.meteo.Repositories.StateRepository;
import com.meteo.meteo.Utils.DataLoader;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ComponentScan("com.meteo.meteo.Utils")
@AutoConfigureMockMvc
public class TestControllers {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @org.junit.Before
    public void setUpData() {
        dataLoader.loadData("data_set");
    }

    private MvcResult fetchData(String url) throws Exception {
        return this.mvc.perform(MockMvcRequestBuilders
                .get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void dataListTest() throws Exception {
        MvcResult result = fetchData("/api/listMeasurement");

        String content = result.getResponse().getContentAsString();
        JSONArray jsonarray = new JSONArray(content);
        JSONObject object = (JSONObject) jsonarray.get(0);

        Assert.assertEquals("Berlin", object.get("stateName"));
        Assert.assertEquals("broken clouds", object.get("weatherDescription"));

        result = fetchData("/api/listTown");

        content = result.getResponse().getContentAsString();
        jsonarray = new JSONArray(content);
        object = (JSONObject) jsonarray.get(0);

        Assert.assertEquals("Berlin", object.get("name"));
        Assert.assertEquals((Double) 13.3888599, (Double) ((BigDecimal) object.get("longitude")).doubleValue());
        Assert.assertEquals((Double) 52.5170365, (Double) ((BigDecimal) object.get("latitude")).doubleValue());
    }

    @Test
    public void addTownWithMeasurementTest() throws Exception {
        StateEntity state = this.stateRepository.findByName("Paris");
        Assert.assertNull(state);

        fetchData(
                "/api/addTown?town=Paris&country=FR&state=Ile-de-France&latitude=48.8588897&longitude=2.3200410217200766");

        state = this.stateRepository.findByName("Paris");
        Assert.assertNotNull(state);

        List<MeasurementEntity> measurements = this.measurementRepository.findByIdState("Paris");
        Assert.assertEquals(0, measurements.size());

        fetchData(
                "/api/addMeasurement?town=Paris&weather=Clear&description=clear sky&temperature=288.45&humidity=63&pressure=1019&windSpeed=4.63&windDegree=70&clouds=0&saved=2023-05-04T11:27:47.639464840");

        measurements = this.measurementRepository.findByIdState("Paris");
        Assert.assertEquals(1, measurements.size());
        Assert.assertEquals(LocalDateTime.parse("2023-05-04T11:27:47.639465"), measurements.get(0).getSaved());
    }

    @Test
    public void editTownAndMeasurementTest() throws Exception {
        fetchData("/api/editTown?town=Berlin&state=upravenahodnota&longitude=22.3200410217200766");

        StateEntity state = this.stateRepository.findByName("Berlin");
        Assert.assertEquals("upravenahodnota", state.getState());
        Assert.assertEquals((Double) 22.3200410217200766, state.getLongitude());

        fetchData("/api/editMeasurement?town=Berlin&temperature=555.55&saved=2023-05-02T19:10:40.750193");

        MeasurementEntity measurementEntity = this.measurementRepository
                .findById(LocalDateTime.parse("2023-05-02T19:10:40.750193"), "Berlin");
        Assert.assertEquals((Double) 555.55, measurementEntity.getTemperature());
    }

    @Test
    public void deleteTownAndMeasurement() throws Exception {
        fetchData("/api/deleteTown?town=Berlin");

        StateEntity state = this.stateRepository.findByName("Berlin");
        Assert.assertNull(state);

        fetchData("/api/deleteMeasurement?town=Berlin&saved=2023-05-02T19:10:40.750193");

        MeasurementEntity measurementEntity = this.measurementRepository
                .findById(LocalDateTime.parse("2023-05-02T19:10:40.750193"), "Berlin");
        Assert.assertNull(measurementEntity);
    }
}
