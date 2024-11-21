package com.viniciusdalaqua.GateControl.services;

import com.viniciusdalaqua.GateControl.entities.Driver;
import com.viniciusdalaqua.GateControl.entities.Vehicle;
import com.viniciusdalaqua.GateControl.repositories.DriverRepository;
import com.viniciusdalaqua.GateControl.repositories.VehicleRepository;
import com.viniciusdalaqua.GateControl.services.exception.DataBaseException;
import com.viniciusdalaqua.GateControl.services.exception.ResourceNotFoundException;
import com.viniciusdalaqua.GateControl.services.exception.VehicleNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @InjectMocks
    private VehicleService vehicleService;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private DriverRepository driverRepository;

    @Test
    @DisplayName("Should return all vehicles for a given owner ID")
    void shouldGetAllVehiclesWithOwnerId() {
        Vehicle vehicle = new Vehicle(1L, "ABCD1234", "Camaro", "Yellow",
                new Driver(1L, "Vinicius Henrique", "111111111", "14999999999"));

        when(vehicleRepository.findByDriverId(1L)).thenReturn(Collections.singletonList(vehicle));

        List<Vehicle> vehicles = vehicleService.findByDriverId(1L);

        assertNotNull(vehicles);
        assertEquals(1, vehicles.size());
        assertEquals(vehicle, vehicles.get(0));
        verify(vehicleRepository, times(1)).findByDriverId(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when owner ID is not found")
    void shouldThrowExceptionWhenOwnerIdNotFound() {
        when(vehicleRepository.findByDriverId(1L)).thenThrow(EntityNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.findByDriverId(1L));
        verify(vehicleRepository, times(1)).findByDriverId(1L);
    }

    @Test
    @DisplayName("Should throw VehicleNotFoundException when vehicle plate is not found")
    void shouldThrowExceptionWhenVehiclePlateNotFound() {
        String plate = "ABCD1234";
        when(vehicleRepository.findByPlate(plate)).thenReturn(Optional.empty());

        VehicleNotFoundException exception = assertThrows(VehicleNotFoundException.class,
                () -> vehicleService.findByPlate(plate));
        assertEquals(String.format("vehicle with plate %s not found", plate), exception.getMessage());
        verify(vehicleRepository, times(1)).findByPlate(plate);
    }

    @Test
    @DisplayName("Should return a vehicle by its ID")
    void shouldReturnVehicleById() {
        Vehicle vehicle = new Vehicle(1L, "ABCD1234", "Camaro", "Yellow",
                new Driver(1L, "Vinicius Henrique", "123456789", "88888888888"));

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        Vehicle result = vehicleService.findById(1L);

        assertNotNull(result);
        assertEquals(vehicle, result);
        verify(vehicleRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when vehicle ID is not found")
    void shouldThrowExceptionWhenVehicleIdNotFound() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> vehicleService.findById(1L));
        assertEquals("Resource not found. id: 1", exception.getMessage());
        verify(vehicleRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should insert a new vehicle successfully")
    void shouldInsertVehicleSuccessfully() {
        Vehicle vehicle = new Vehicle(1L, "ABCD1234", "Jeep Renegade", "Black",
                new Driver(1L, "Vinicius Henrique", "123456789", "88888888888"));

        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        Vehicle result = vehicleService.insert(vehicle);

        assertNotNull(result);
        assertEquals(vehicle, result);
        verify(vehicleRepository, times(1)).save(vehicle);
    }

    @Test
    @DisplayName("Should throw DataBaseException for duplicate plate during insertion")
    void shouldThrowExceptionForDuplicatePlateOnInsert() {
        Vehicle vehicle = new Vehicle(1L, "ABC-1234", "Toyota", "Gray",
                new Driver(1L, "Vinicius Henrique", "123456789", "88888888888"));

        Vehicle newVehicle = new Vehicle(2L, "ABC-1234", "Toyota", "Gray",
                new Driver(1L, "Vinicius Henrique", "123456789", "88888888888"));

        when(vehicleRepository.findByPlate("ABC-1234")).thenReturn(Optional.of(vehicle));

        DataBaseException exception = assertThrows(DataBaseException.class,
                () -> vehicleService.insert(newVehicle));
        assertEquals("ABC-1234 already exists", exception.getMessage());
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update a vehicle successfully")
    void shouldUpdateVehicleSuccessfully() {
        Driver driver = new Driver(1L, "Vinicius Henrique", "123456789", "88888888888");
        Vehicle oldVehicle = new Vehicle(1L, "ABC-1234", "Toyota", "Gray", driver);
        Vehicle updatedVehicle = new Vehicle(1L, "ABC-1234", "Corolla", "Gray", driver);

        when(vehicleRepository.getReferenceById(1L)).thenReturn(oldVehicle);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(updatedVehicle);

        Vehicle result = vehicleService.update(1L, updatedVehicle);

        assertNotNull(result);
        assertEquals(updatedVehicle, result);
        verify(vehicleRepository, times(1)).save(updatedVehicle);
    }

    @Test
    @DisplayName("Should delete a vehicle successfully")
    void shouldDeleteVehicleSuccessfully() {
        vehicleService.delete(1L);
        verify(vehicleRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting a non-existent vehicle")
    void shouldThrowExceptionWhenDeletingNonExistentVehicle() {
        doThrow(new EmptyResultDataAccessException(1)).when(vehicleRepository).deleteById(1L);

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.delete(1L));
        verify(vehicleRepository, times(1)).deleteById(1L);
    }
}
