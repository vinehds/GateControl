package com.viniciusdalaqua.GateControl.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_vehicles")
public class Vehicle implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String plate;

    private String model;

    private String color;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Driver owner;

    @JsonIgnore
    @OneToOne(mappedBy = "vehicle")
    private RecordLog recordLog;

    public RecordLog getRecordLog() {
        return recordLog;
    }

    public void setRecordLog(RecordLog recordLog) {
        this.recordLog = recordLog;
    }

    public Vehicle() {}

    public Vehicle(Long id, String plate, String model, String color, Driver owner) {
        this.id = id;
        this.plate = plate;
        this.model = model;
        this.color = color;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Driver getOwner() {
        return owner;
    }

    public void setOwner(Driver owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(id, vehicle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
