package com.viniciusdalaqua.GateControl.services;

import com.viniciusdalaqua.GateControl.entities.RecordLog;
import com.viniciusdalaqua.GateControl.repositories.RecordLogRepository;
import com.viniciusdalaqua.GateControl.services.exception.DataBaseException;
import com.viniciusdalaqua.GateControl.services.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecordLogService {

    private RecordLogRepository repository;

    public RecordLogService(RecordLogRepository repository) {
        this.repository = repository;
    }

    public List<RecordLog> findAll(){
        return repository.findAll();
    }

    public RecordLog findById(Long id){
        Optional<RecordLog> recordFound = repository.findById(id);
        return recordFound.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public RecordLog insert(RecordLog obj){
        return repository.save(obj);
    }

    public RecordLog update(Long id, RecordLog obj){
        try {
            RecordLog entity = repository.getReferenceById(id);
            loadData(entity, obj);
            return repository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    public void delete(Long id){
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DataBaseException(e.getMessage());
        }
    }

    private void loadData(RecordLog entity, RecordLog obj) {
        entity.setRecordType(obj.getRecordType());
        entity.setDate(obj.getDate());
        entity.setRemark(obj.getRemark());
    }
}
