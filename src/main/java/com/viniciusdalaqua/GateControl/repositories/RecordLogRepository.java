package com.viniciusdalaqua.GateControl.repositories;

import com.viniciusdalaqua.GateControl.entities.RecordLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordLogRepository extends JpaRepository<RecordLog, Long> {
}
