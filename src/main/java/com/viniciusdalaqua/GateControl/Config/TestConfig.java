package com.viniciusdalaqua.GateControl.Config;

import com.viniciusdalaqua.GateControl.entities.Driver;
import com.viniciusdalaqua.GateControl.entities.RecordLog;
import com.viniciusdalaqua.GateControl.entities.Vehicle;
import com.viniciusdalaqua.GateControl.entities.enuns.RecordType;
import com.viniciusdalaqua.GateControl.repositories.DriverRepository;
import com.viniciusdalaqua.GateControl.repositories.RecordLogRepository;
import com.viniciusdalaqua.GateControl.repositories.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class TestConfig implements CommandLineRunner {

    private final DriverRepository driverRepository;

    private final VehicleRepository vehicleRepository;

    private final RecordLogRepository recordRepository;

    public TestConfig(DriverRepository driverRepository, VehicleRepository vehicleRepository, RecordLogRepository recordRepository) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.recordRepository = recordRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Driver driver = new Driver(null, "2626262262", "Joãozinho", "14998067966");

        Vehicle v1 = new Vehicle(null, "IMB-1669", "Jeep", "red", driver);

        driver.addVehicle(v1);

        driverRepository.save(driver);
        vehicleRepository.save(v1);

        RecordLog rl1 = new RecordLog(null, RecordType.OUT, LocalDateTime.now(), v1, v1.getOwner(), "...");
        recordRepository.save(rl1);

    }
}
