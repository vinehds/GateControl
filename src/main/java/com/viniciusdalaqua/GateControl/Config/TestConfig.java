package com.viniciusdalaqua.GateControl.Config;

import com.viniciusdalaqua.GateControl.entities.Driver;
import com.viniciusdalaqua.GateControl.repositories.DriverRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig implements CommandLineRunner {

    private final DriverRepository driverRepository;

    public TestConfig(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Driver driver = new Driver(null, "2626262262", "Joãozinho", "14998067966");

        driverRepository.save(driver);
    }
}
