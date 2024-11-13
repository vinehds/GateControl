package com.viniciusdalaqua.GateControl.repositories;

import com.viniciusdalaqua.GateControl.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository <Vehicle, Long>{
}
