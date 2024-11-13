package com.viniciusdalaqua.GateControl.resources;

import com.viniciusdalaqua.GateControl.entities.Vehicle;
import com.viniciusdalaqua.GateControl.services.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/vehicles")
public class VehicleResource {

    private VehicleService vehicleService;

    public VehicleResource(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> findAll(){
        List<Vehicle> vehicles = vehicleService.findAll();
        return ResponseEntity.ok().body(vehicles);
    }

}
