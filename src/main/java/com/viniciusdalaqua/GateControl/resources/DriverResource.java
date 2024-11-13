package com.viniciusdalaqua.GateControl.resources;

import com.viniciusdalaqua.GateControl.entities.Driver;
import com.viniciusdalaqua.GateControl.services.DriverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/drivers")
public class DriverResource {

    private final DriverService driverService;

    public DriverResource(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public ResponseEntity<List<Driver>> findAll(){
        List<Driver> drivers = driverService.findAll();
        return ResponseEntity.ok().body(drivers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> findById(@PathVariable Long id){
        Driver driverFound = driverService.findById(id);
        return ResponseEntity.ok().body(driverFound);
    }

    @PostMapping
    public ResponseEntity<Driver> insert( @RequestBody Driver driver){
        Driver driverInserted = driverService.insert(driver);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(driverInserted.getId())
                .toUri();

        return ResponseEntity.created(uri).body(driverInserted);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Driver> update(@PathVariable long id, @RequestBody Driver obj){
        obj = driverService.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        driverService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
