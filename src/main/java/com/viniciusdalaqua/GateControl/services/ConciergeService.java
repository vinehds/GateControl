package com.viniciusdalaqua.GateControl.services;

import com.viniciusdalaqua.GateControl.entities.Concierge;
import com.viniciusdalaqua.GateControl.repositories.ConciergeRepository;
import com.viniciusdalaqua.GateControl.services.exception.DataBaseException;
import com.viniciusdalaqua.GateControl.services.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
        return concierge.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Concierge insert(Concierge concierge){
        return conciergeRepository.save(concierge);
    }

    public Concierge update(Long id, Concierge obj){
        try {
            Concierge entity = conciergeRepository.getReferenceById(id);
            loadData(entity, obj);
            return conciergeRepository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    public void delete(Long id){
        try {
            conciergeRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DataBaseException(e.getMessage());
        }
    }

    private void loadData(Concierge entity, Concierge obj) {
        entity.setName(obj.getName());
        entity.setLocation(obj.getLocation());
    }

}
