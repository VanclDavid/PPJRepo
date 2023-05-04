package com.meteo.meteo.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "measurements")
public class MeasurementEntity {
    @EmbeddedId
    private MeasurementId id;

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

    private LocalDateTime expires;

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }

    public LocalDateTime getSaved() {
        return id.getSaved();
    }

    public void setSaved(LocalDateTime saved) {
        if (id == null) {
            id = new MeasurementId();
        }
        id.setSaved(saved);
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

    @JsonIgnore
    public String getState() {
        return id.getState();
    }

    public String getStateName() {
        return id.getState();
    }

    @JsonProperty(value = "name")
    public void setState(StateEntity state) {
        if (id == null) {
            id = new MeasurementId();
        }
        id.setState(state.getName());
    }

    @JsonIgnore
    public ArrayList<String> getHeader() {
        ArrayList<String> list = new ArrayList<String>();

        list.add("state");
        list.add("weather_main");
        list.add("weather_description");
        list.add("temperature");
        list.add("humidity");
        list.add("pressure");
        list.add("wind_speed");
        list.add("wind_degree");
        list.add("clouds");
        list.add("saved");
        list.add("expires");

        return list;
    }

    @JsonIgnore
    public ArrayList<String> getData() {
        ArrayList<String> list = new ArrayList<String>();

        list.add((id.getState() != null) ? id.getState() : "");
        list.add((weatherMain != null) ? weatherMain : "");
        list.add((weatherDescription != null) ? weatherDescription : "");
        list.add((temperature != null) ? temperature.toString() : "");
        list.add((humidity != null) ? humidity.toString() : "");
        list.add((pressure != null) ? pressure.toString() : "");
        list.add((windSpeed != null) ? windSpeed.toString() : "");
        list.add((windDegree != null) ? windDegree.toString() : "");
        list.add((clouds != null) ? clouds.toString() : "");
        list.add((id.getSaved() != null) ? id.getSaved().toString() : "");
        list.add((expires != null) ? expires.toString() : "");

        return list;
    }
}
