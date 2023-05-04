package com.meteo.meteo.Models;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.*;

public class MeasurementId implements Serializable {
    @Column(name = "saved", nullable = false)
    private LocalDateTime saved;

    @JoinColumn(name = "name", foreignKey = @ForeignKey(name = "state_fk", value = ConstraintMode.NO_CONSTRAINT))
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getSaved() {
        return saved;
    }

    public void setSaved(LocalDateTime saved) {
        this.saved = saved;
    }
}
