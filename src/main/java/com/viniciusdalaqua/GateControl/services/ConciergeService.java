package com.viniciusdalaqua.GateControl.services;

import com.viniciusdalaqua.GateControl.entities.Concierge;
import com.viniciusdalaqua.GateControl.repositories.ConciergeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ConciergeService {

    private final ConciergeRepository conciergeRepository;

    public ConciergeService(ConciergeRepository conciergeRepository) {
        this.conciergeRepository = conciergeRepository;
    }

    public List<Concierge> findAll(){
        return conciergeRepository.findAll();
    }

    public Concierge findById(Long id){
        Optional<Concierge> concierge = conciergeRepository.findById(id);
        return concierge.orElseThrow();
    }

    public Concierge insert(Concierge concierge){
        return conciergeRepository.save(concierge);
    }

    public Concierge update(Long id, Concierge obj){
        Concierge entity = conciergeRepository.getReferenceById(id);
        loadData(entity, obj);
        return conciergeRepository.save(entity);
    }

    private void loadData(Concierge entity, Concierge obj) {
        entity.setName(obj.getName());
        entity.setLocation(obj.getLocation());
    }

    public void delete(Long id){
        conciergeRepository.deleteById(id);
    }

}
