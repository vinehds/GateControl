package com.viniciusdalaqua.GateControl.repositories;

import com.viniciusdalaqua.GateControl.entities.Driver;
import com.viniciusdalaqua.GateControl.entities.Vehicle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class VehicleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VehicleRepository vehicleRepository;


    @Test
    @DisplayName("Should get Drivers with the owner id from DB")
    void findByDriverIdSuccess() {
        var vehicle = new Vehicle(null, "ABE5904", "Car test", "red",
                new Driver(null, "Driver Name", "999999999", "14999999999"));

        this.createVehicle(vehicle);

        List<Vehicle> vehicleListTest = vehicleRepository.findByDriverId(vehicle.getOwner().getId());

        Assertions.assertEquals(1, vehicleListTest.size());
        Assertions.assertEquals(vehicle, vehicleListTest.get(0));
    }

    @Test
    @DisplayName("Should get Drivers with the owner id from DB")
    void findByDriverIdError() {

        List<Vehicle> vehicleListTest = vehicleRepository.findByDriverId(1L);

        assertThat(vehicleListTest).isEmpty();
    }


    @Test
    @DisplayName("Should get Vehicle with the plate from DB")
    void findByPlateSuccess() {
        String plate = "ABED905";
        var vehicle = new Vehicle(null, plate, "Car test", "red",
                new Driver(null, "Driver Name", "999999999", "14999999999"));

        this.createVehicle(vehicle);

        Optional<Vehicle> result = this.vehicleRepository.findByPlate(plate);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("It shouldn't get Driver with the DB cpf when the user doesn't exist")
    void findByPlateError() {
        String plate = "ABED905";

        Optional<Vehicle> result = this.vehicleRepository.findByPlate(plate);

        assertThat(result.isEmpty()).isTrue();
    }

    private Vehicle createVehicle(Vehicle vehicleTest) {
        this.entityManager.persist(vehicleTest.getOwner());
        this.entityManager.persist(vehicleTest);
        return vehicleTest;
    }

}