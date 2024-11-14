package com.viniciusdalaqua.GateControl.resources;

import com.viniciusdalaqua.GateControl.entities.Concierge;
import com.viniciusdalaqua.GateControl.services.ConciergeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/concierges")
public class ConciergeResource {

    private final ConciergeService conciergeService;

    public ConciergeResource(ConciergeService conciergeService) {
        this.conciergeService = conciergeService;
    }

    @GetMapping
    public ResponseEntity<List<Concierge>> findAll(){
        List<Concierge> concierges = conciergeService.findAll();
        return ResponseEntity.ok().body(concierges);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Concierge> findById(@PathVariable Long id){
        Concierge concierge = conciergeService.findById(id);
        return ResponseEntity.ok().body(concierge);
    }

    @PostMapping
    public ResponseEntity<Concierge> insert(@RequestBody Concierge concierge){
        Concierge conciergeInserted = conciergeService.insert(concierge);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(conciergeInserted)
                .toUri();

        return ResponseEntity.created(uri).body(conciergeInserted);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Concierge> update(@PathVariable Long id, @RequestBody Concierge obj){
        obj = conciergeService.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        conciergeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
