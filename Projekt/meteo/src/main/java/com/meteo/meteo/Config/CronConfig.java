package com.meteo.meteo.Config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import com.meteo.meteo.Models.StateEntity;
import com.meteo.meteo.Repositories.StateRepository;
import com.meteo.meteo.Utils.LogUtil;
import com.meteo.meteo.Utils.OpenWeatherApi;

@Configuration
@EnableScheduling
public class CronConfig {
    @Value("${spring.cron.enabled}")
    private boolean isEnabled;

    @Autowired
    private OpenWeatherApi openWeatherApi;

    @Autowired
    private LogUtil logger;

    @Autowired
    private StateRepository stateRepository;

    @Scheduled(fixedRateString = "${spring.cron.rate.in.milliseconds}")
    public void cronDataLoader() {
        if (!this.isEnabled) {
            return;
        }

        this.logger.log("Cron [DOWNLOAD MEASUREMENTS] started");
        try {
            List<StateEntity> states = this.stateRepository.findAll();
            states.forEach(entity -> {
                try {
                    this.openWeatherApi.downloadMeasurement(entity);
                } catch (Exception e) {
                    this.logger.logException(e);
                }
            });
        } catch (Exception e) {
            this.logger.logException(e);
        }

        this.logger.log("Cron [DOWNLOAD MEASUREMENTS] successfully completed.");
    }
}
