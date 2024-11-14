package com.viniciusdalaqua.GateControl.services;

import com.viniciusdalaqua.GateControl.entities.Driver;
import com.viniciusdalaqua.GateControl.repositories.DriverRepository;
import com.viniciusdalaqua.GateControl.services.exception.DataBaseException;
import com.viniciusdalaqua.GateControl.services.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public List<Driver> findAll() {
        return driverRepository.findAll();
    }

    public Driver findById(Long id) {
        Optional<Driver> driver = driverRepository.findById(id);
        return driver.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Driver insert(Driver driver) {
        return driverRepository.save(driver);
    }

    public Driver update(Long id, Driver obj) {
        try {
            Driver entity = driverRepository.getReferenceById(id);
            updateData(entity, obj);
            return driverRepository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    public void delete(Long id){
        try {
            driverRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DataBaseException(e.getMessage());
        }
    }

    public void updateData(Driver entity, Driver obj) {
        entity.setName(obj.getName());
        entity.setCnh(obj.getCnh());
        entity.setPhone(obj.getPhone());
    }
}
