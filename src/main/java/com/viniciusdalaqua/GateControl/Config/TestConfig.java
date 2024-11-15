package com.viniciusdalaqua.GateControl.Config;

import com.viniciusdalaqua.GateControl.entities.Concierge;
import com.viniciusdalaqua.GateControl.entities.Driver;
import com.viniciusdalaqua.GateControl.entities.RecordLog;
import com.viniciusdalaqua.GateControl.entities.Vehicle;
import com.viniciusdalaqua.GateControl.entities.enuns.RecordType;
import com.viniciusdalaqua.GateControl.repositories.ConciergeRepository;
import com.viniciusdalaqua.GateControl.repositories.DriverRepository;
import com.viniciusdalaqua.GateControl.repositories.RecordLogRepository;
import com.viniciusdalaqua.GateControl.repositories.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class TestConfig implements CommandLineRunner {

    private final DriverRepository driverRepository;

    private final VehicleRepository vehicleRepository;

    private final RecordLogRepository recordRepository;

    private final ConciergeRepository conciergeRepository;

    public TestConfig(DriverRepository driverRepository, VehicleRepository vehicleRepository, RecordLogRepository recordRepository, ConciergeRepository conciergeRepository) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.recordRepository = recordRepository;
        this.conciergeRepository = conciergeRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        Driver driver = new Driver(null, "Joãozinho", "6265156156", "14998067966");
        Driver driver2 = new Driver(null, "Vine dalaqua", "55555555", "14998067966");
        Driver driver3 = new Driver(null, "Maria", "56156165656", "14998067966");

        Vehicle v1 = new Vehicle(null, "IMB-1669", "Jeep", "red", driver);
        Vehicle v2 = new Vehicle(null, "IMB-1669", "Ferrari", "red", driver);
        Vehicle v3 = new Vehicle(null, "IMB-1669", "Civic", "red", driver2);
        Vehicle v4 = new Vehicle(null, "IMB-1669", "Gol", "red", driver3);

        driver.addVehicle(v1);

        driverRepository.saveAll(Arrays.asList(driver, driver2, driver3) );
        vehicleRepository.saveAll(Arrays.asList(v1, v2, v3, v4));

        Concierge concierge1 = new Concierge(null, "Portaria das Flores", "Lençóis Palista - SP");
        Concierge concierge2 = new Concierge(null, "Portaria das arvortes", "Lençóis Palista - SP");
        Concierge concierge3 = new Concierge(null, "Portaria das cidades", "Lençóis Palista - SP");
        Concierge concierge4 = new Concierge(null, "Portaria das cidades", "Lençóis Palista - SP");
        Concierge concierge6 = new Concierge(null, "Portaria das cidades", "Lençóis Palista - SP");
        Concierge concierge5 = new Concierge(null, "Portaria das cidades", "Lençóis Palista - SP");
        Concierge concierge7 = new Concierge(null, "Portaria das cidades", "Lençóis Palista - SP");
        Concierge concierge8 = new Concierge(null, "Portaria das cidades", "Lençóis Palista - SP");
        Concierge concierge9 = new Concierge(null, "Portaria das cidades", "Lençóis Palista - SP");
        Concierge concierge0 = new Concierge(null, "Portaria das cidades", "Lençóis Palista - SP");
        Concierge concierge01 = new Concierge(null, "Portaria das cidades", "Lençóis Palista - SP");
        Concierge concierge02 = new Concierge(null, "Portaria das cidades", "Lençóis Palista - SP");
        Concierge concierge03 = new Concierge(null, "Portaria das cidades", "Lençóis Palista - SP");
        Concierge concierge04 = new Concierge(null, "Portaria das cidades", "Lençóis Palista - SP");


        RecordLog r1 = new RecordLog(null, RecordType.OUT, LocalDateTime.now(), v1, v1.getOwner(), "...", concierge1);
        RecordLog r2 = new RecordLog(null, RecordType.IN, LocalDateTime.now(), v1, v1.getOwner(), "...", concierge1);
        RecordLog r3 = new RecordLog(null, RecordType.IN, LocalDateTime.now(), v2, v2.getOwner(), "...", concierge1);
        RecordLog r4 = new RecordLog(null, RecordType.OUT, LocalDateTime.now(), v3, v3.getOwner(), "...", concierge1);
        RecordLog r5 = new RecordLog(null, RecordType.OUT, LocalDateTime.now(), v4, v4.getOwner(), "...", concierge1);
        RecordLog r6 = new RecordLog(null, RecordType.IN, LocalDateTime.now(), v3, v3.getOwner(), "...", concierge1);
        concierge1.addRecord(r1);
        concierge1.addRecord(r2);
        concierge1.addRecord(r3);
        concierge1.addRecord(r4);
        concierge1.addRecord(r5);
        concierge1.addRecord(r6);

        conciergeRepository.saveAll(Arrays.asList(concierge1, concierge2, concierge3, concierge4));
        recordRepository.saveAll(Arrays.asList(r1, r2, r3, r4, r5, r6));

    }
}
