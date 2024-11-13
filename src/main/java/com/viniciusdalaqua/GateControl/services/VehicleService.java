package com.viniciusdalaqua.GateControl.services;

import com.viniciusdalaqua.GateControl.entities.Vehicle;
import com.viniciusdalaqua.GateControl.repositories.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }
}
