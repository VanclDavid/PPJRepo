package com.meteo.meteo.Models;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "measurements")
public class MeasurementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "state_fk", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private StateEntity state;

    @Column(nullable = true)
    private String weatherMain;

    @Column(nullable = true)
    private String weatherDescription;

    @Column(nullable = true)
    private Double temperature;

    @Column(nullable = true)
    private Integer humidity;

    @Column(nullable = true)
    private Integer pressure;

    @Column(nullable = true)
    private Double windSpeed;

    @Column(nullable = true)
    private Double windDegree;

    @Column(nullable = true)
    private Double clouds;

    private LocalDateTime saved;

    public LocalDateTime getSaved() {
        return saved;
    }

    public void setSaved(LocalDateTime saved) {
        this.saved = saved;
    }

    public Double getClouds() {
        return clouds;
    }

    public void setClouds(Double clouds) {
        this.clouds = clouds;
    }

    public Double getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(Double windDegree) {
        this.windDegree = windDegree;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public void setWeatherMain(String weatherMmain) {
        this.weatherMain = weatherMmain;
    }

    public StateEntity getState() {
        return state;
    }

    public void setState(StateEntity state) {
        this.state = state;
    }
}
