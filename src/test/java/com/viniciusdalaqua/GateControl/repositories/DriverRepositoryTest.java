package com.viniciusdalaqua.GateControl.repositories;

import com.viniciusdalaqua.GateControl.entities.Driver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class DriverRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DriverRepository driverRepository;


    @Test
    @DisplayName("Should get Driver with the cpf from DB")
    void findByCnhSuccess() {
        String cnh = "99999999999";
        Driver driverTest = new Driver(null, "Driver Name", cnh, "14999999999");
        this.createDriver(driverTest);

        Optional<Driver> result = this.driverRepository.findByCnh(cnh);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("It shouldn't get Driver with the DB cpf when the user doesn't exist")
    void findByCnhError() {
        String cnh = "99999999999";

        Optional<Driver> result = this.driverRepository.findByCnh(cnh);

        assertThat(result.isEmpty()).isTrue();
    }


    private Driver createDriver(Driver driver) {
        this.entityManager.persist(driver);
        return driver;
    }
}