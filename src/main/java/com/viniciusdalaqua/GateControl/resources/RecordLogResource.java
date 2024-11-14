package com.viniciusdalaqua.GateControl.resources;

import com.viniciusdalaqua.GateControl.entities.RecordLog;
import com.viniciusdalaqua.GateControl.services.RecordLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/records")
public class RecordLogResource {

    private final RecordLogService service;

    public RecordLogResource(RecordLogService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RecordLog>> findAll(){
        List<RecordLog> records = service.findAll();
        return ResponseEntity.ok().body(records);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RecordLog> findById(@PathVariable Long id){
        RecordLog obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<RecordLog> insert(@RequestBody RecordLog obj){
        RecordLog record = service.insert(obj);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(record.getId())
                .toUri();

        return ResponseEntity.created(uri).body(record);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<RecordLog> update(@PathVariable Long id, @RequestBody RecordLog obj){
        obj = service.update(id,obj);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
