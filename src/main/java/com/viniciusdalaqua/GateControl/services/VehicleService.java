package com.viniciusdalaqua.GateControl.services;

import com.viniciusdalaqua.GateControl.entities.Vehicle;
import com.viniciusdalaqua.GateControl.repositories.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public Vehicle findById(Long id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        return vehicle.orElseThrow();
    }

    public Vehicle insert(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle update(Long id, Vehicle obj) {
        Vehicle entity = vehicleRepository.getReferenceById(id);
        updateData(entity, obj);
        return vehicleRepository.save(entity);
    }

    public void updateData(Vehicle entity, Vehicle obj) {
        entity.setPlate(obj.getPlate());
        entity.setModel(obj.getModel());
        entity.setColor(obj.getColor());
        entity.setOwner(obj.getOwner());
    }

    public void delete(Long id){
        vehicleRepository.deleteById(id);
    }

}
