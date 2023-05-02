package com.meteo.meteo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.meteo.meteo.Models.StateEntity;
import com.meteo.meteo.Repositories.StateRepository;
import com.meteo.meteo.Utils.DataLoader;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@ComponentScan("com.meteo.meteo.Utils")
public class RepositoryTest {
    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private DataLoader dataLoader;

    @org.junit.Before
    public void setUpData() {
        dataLoader.loadData("states");
    }

    @Test
    public void testDataExists() {
        StateEntity london = this.stateRepository.findByName("London");
        Assert.assertEquals("London", london.getName());
        Assert.assertEquals("England", london.getState());

        StateEntity brno = this.stateRepository.findByName("Brno");
        Assert.assertEquals("Brno", brno.getName());
        Assert.assertEquals("Southeast", brno.getState());
    }
}