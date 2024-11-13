package com.viniciusdalaqua.GateControl.Config;

import com.viniciusdalaqua.GateControl.entities.Driver;
import com.viniciusdalaqua.GateControl.entities.Vehicle;
import com.viniciusdalaqua.GateControl.repositories.DriverRepository;
import com.viniciusdalaqua.GateControl.repositories.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig implements CommandLineRunner {

    private final DriverRepository driverRepository;

    private final VehicleRepository vehicleRepository;

    public TestConfig(DriverRepository driverRepository, VehicleRepository vehicleRepository) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Driver driver = new Driver(null, "2626262262", "Joãozinho", "14998067966");
        driverRepository.save(driver);

        Vehicle v1 = new Vehicle(null, "IMB-1669", "Jeep", "red", driver);

        driver.addVehicle(v1);

        driverRepository.save(driver);
        vehicleRepository.save(v1);
    }
}
