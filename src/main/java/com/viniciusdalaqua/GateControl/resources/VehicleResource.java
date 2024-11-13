package com.viniciusdalaqua.GateControl.resources;

import com.viniciusdalaqua.GateControl.entities.Vehicle;
import com.viniciusdalaqua.GateControl.services.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/vehicles")
public class VehicleResource {

    private final VehicleService vehicleService;

    public VehicleResource(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> findAll(){
        List<Vehicle> vehicles = vehicleService.findAll();
        return ResponseEntity.ok().body(vehicles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> findById(@PathVariable Long id){
        Vehicle obj = vehicleService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Vehicle> insert( @RequestBody Vehicle vehicle){
        Vehicle vehicleInserted = vehicleService.insert(vehicle);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(vehicleInserted.getId())
                .toUri();

        return ResponseEntity.created(uri).body(vehicleInserted);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Vehicle> update(@PathVariable long id, @RequestBody Vehicle obj){
        obj = vehicleService.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
