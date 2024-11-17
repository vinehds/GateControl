package com.viniciusdalaqua.GateControl.entities;

import com.viniciusdalaqua.GateControl.entities.enuns.RecordType;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_record_log")
public class RecordLog implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer recordType;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    private String remark;

    public RecordLog() {}

    public RecordLog(Long id, RecordType recordType, LocalDateTime date, Vehicle vehicle, Driver driver, String remark) {
        this.id = id;
        setRecordType(recordType);
        this.date = date;
        this.vehicle = vehicle;
        this.driver = driver;
        this.remark = remark;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RecordType getRecordType() {
       return RecordType.valueOf(recordType);
    }

    public void setRecordType(RecordType recordType) {
        if(recordType != null) {
            this.recordType = recordType.getCode();
        }
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordLog recordLog = (RecordLog) o;
        return Objects.equals(id, recordLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
