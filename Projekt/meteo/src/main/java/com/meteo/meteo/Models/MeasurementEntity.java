package com.meteo.meteo.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "measurements")
public class MeasurementEntity {
    @PrimaryKeyJoinColumn
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

    @Id
    private LocalDateTime saved;

    private LocalDateTime expires;

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }

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

    @JsonIgnore
    public StateEntity getState() {
        return state;
    }

    public String getStateName() {
        return state.getName();
    }

    @JsonProperty(value = "name")
    public void setState(StateEntity state) {
        this.state = state;
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

        list.add((state != null) ? state.getName() : "");
        list.add((weatherMain != null) ? weatherMain : "");
        list.add((weatherDescription != null) ? weatherDescription : "");
        list.add((temperature != null) ? temperature.toString() : "");
        list.add((humidity != null) ? humidity.toString() : "");
        list.add((pressure != null) ? pressure.toString() : "");
        list.add((windSpeed != null) ? windSpeed.toString() : "");
        list.add((windDegree != null) ? windDegree.toString() : "");
        list.add((clouds != null) ? clouds.toString() : "");
        list.add((saved != null) ? saved.toString() : "");
        list.add((expires != null) ? expires.toString() : "");

        return list;
    }
}
