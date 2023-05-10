package com.meteo.meteo;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.meteo.meteo.Models.MeasurementEntity;
import com.meteo.meteo.Models.StateEntity;
import com.meteo.meteo.Repositories.MeasurementRepository;
import com.meteo.meteo.Repositories.StateRepository;
import com.meteo.meteo.Utils.DataLoader;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@ComponentScan("com.meteo.meteo.Utils")
public class TestRepository {
    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private DataLoader dataLoader;

    @org.junit.Before
    public void setUpData() {
        dataLoader.loadData("data_set");
    }

    @Test
    public void stateDataTest() {
        StateEntity london = this.stateRepository.findByName("London");
        Assert.assertEquals("London", london.getName());
        Assert.assertEquals("England", london.getState());

        StateEntity brno = this.stateRepository.findByName("Brno");
        Assert.assertEquals("Brno", brno.getName());
        Assert.assertEquals("Southeast", brno.getState());
    }

    @Test
    public void measurementDataTest() {
        List<MeasurementEntity> measurements = this.measurementRepository.findByIdState("Prague");
        Assert.assertEquals((Double) 285.0, measurements.get(0).getTemperature());
        Assert.assertEquals((Integer) 1023, measurements.get(0).getPressure());

        Assert.assertEquals((Double) 275.0, measurements.get(1).getTemperature());
        Assert.assertEquals((Integer) 1028, measurements.get(1).getPressure());
    }
}