package com.viniciusdalaqua.GateControl.services;

import com.viniciusdalaqua.GateControl.entities.RecordLog;
import com.viniciusdalaqua.GateControl.repositories.RecordLogRepository;
import org.springframework.stereotype.Repository;
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
        return recordFound.orElseThrow();
    }

    public RecordLog insert(RecordLog obj){
        return repository.save(obj);
    }

    public RecordLog update(Long id, RecordLog obj){
        RecordLog entity = repository.getReferenceById(id);
        loadData(entity, obj);
        return repository.save(entity);
    }

    private void loadData(RecordLog entity, RecordLog obj) {
        entity.setRecordType(obj.getRecordType());
        entity.setDate(obj.getDate());
        entity.setRemark(obj.getRemark());
    }

    public void delete(Long id){
        repository.deleteById(id);
    }
}
