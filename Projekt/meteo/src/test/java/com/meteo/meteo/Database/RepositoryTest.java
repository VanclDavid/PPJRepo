// package com.meteo.meteo.Database;

// import static org.junit.jupiter.api.Assertions.*;
// import org.junit.Before;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.junit4.SpringRunner;
// import com.meteo.meteo.Models.StateEntity;
// import com.meteo.meteo.Repositories.MeasurementRepository;
// import com.meteo.meteo.Repositories.StateRepository;

// @RunWith(SpringRunner.class)
// @SpringBootTest
// public class RepositoryTest {
// @Autowired
// private MeasurementRepository measurementRepository;

// @Autowired
// private StateRepository stateRepository;

// @Before
// public void setUp() throws Exception {
// StateEntity london = new StateEntity();
// london.setCountry("GB");
// london.setName("London");
// london.setState("England");
// london.setLatitude(51.5073219);
// london.setLongitude(-0.1276474);

// this.stateRepository.save(london);

// // MeasurementEntity liberecEntity = new MeasurementEntity();
// // liberecEntity.set("Czech");
// // liberecEntity.setTown("Liberec");
// // liberecEntity.setMeasurement("30");

// // assertNull(liberecEntity.getId());
// // this.measurementRepository.save(liberecEntity);

// // MeasurementEntity prahaEntity = new MeasurementEntity();
// // prahaEntity.setId(2);
// // prahaEntity.setState("Czech");
// // prahaEntity.setTown("Praha");
// // prahaEntity.setMeasurement("20");

// // assertNull(prahaEntity.getId());
// // this.measurementRepository.save(prahaEntity);
// }

// @Test
// public void testFetchData() {
// // MeasurementEntity liberecEntity =
// // this.measurementRepository.findByTown("Liberec");
// // assertNotNull(liberecEntity);
// // assertEquals("30", liberecEntity.getMeasurement());
// }
// }