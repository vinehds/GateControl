package com.viniciusdalaqua.GateControl.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_concierges")
public class Concierge implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String location;

    @OneToMany(mappedBy = "concierge")
    private final List<RecordLog> records = new java.util.ArrayList<>();

    public Concierge() {}

    public Concierge(Long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<RecordLog> getRecords() {
        return records;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addRecord(RecordLog record) {
        records.add(record);
    }

    public void removeRecord(RecordLog record) {
        records.remove(record);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Concierge concierge = (Concierge) o;
        return Objects.equals(id, concierge.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
