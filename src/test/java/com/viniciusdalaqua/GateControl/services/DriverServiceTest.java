package com.viniciusdalaqua.GateControl.services;

import com.viniciusdalaqua.GateControl.entities.Driver;
import com.viniciusdalaqua.GateControl.repositories.DriverRepository;
import com.viniciusdalaqua.GateControl.services.exception.DataBaseException;
import com.viniciusdalaqua.GateControl.services.exception.ResourceNotFoundException;
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
class DriverServiceTest {

    @InjectMocks
    private DriverService driverService;

    @Mock
    private DriverRepository driverRepository;

    @Test
    @DisplayName("Should return a list with all drivers")
    void findAll_Success() {
        Driver driver = new Driver(1L, "Vinicius", "123456789", "88888888888");
        when(driverRepository.findAll()).thenReturn(Collections.singletonList(driver));

        List<Driver> drivers = driverService.findAll();

        assertNotNull(drivers);
        assertEquals(1, drivers.size());
        assertEquals(driver, drivers.get(0));
        verify(driverRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when driver is not found by ID")
    void findById_Error_NotFound() {
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> driverService.findById(1L));
        assertEquals("Resource not found. id: 1", exception.getMessage());
        verify(driverRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return a driver by ID")
    void findById_Success() {
        Driver driver = new Driver(1L, "Vinicius", "123456789", "88888888888");
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        Driver result = driverService.findById(1L);

        assertNotNull(result);
        assertEquals(driver, result);
        verify(driverRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should successfully insert a new driver")
    void Insert_Success() {
        Driver driver = new Driver(1L, "Vinicius", "123456789", "88888888888");
        when(driverRepository.findByCnh("123456789")).thenReturn(Optional.empty());
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        Driver result = driverService.insert(driver);

        assertNotNull(result);
        assertEquals(driver, result);
        verify(driverRepository, times(1)).save(driver);
    }

    @Test
    @DisplayName("Should throw DataBaseException when trying to insert a duplicate driver")
    void Insert_Error_DriverDuplicate() {
        Driver driver = new Driver(1L, "Vinicius", "123456789", "88888888888");
        when(driverRepository.findByCnh("123456789"))
                .thenReturn(Optional.of(new Driver(2L, "Outro Motorista", "123456789", "99999999999")));

        assertThrows(DataBaseException.class, () -> driverService.insert(driver));
        verify(driverRepository, times(1)).findByCnh("123456789");
    }

    @Test
    @DisplayName("Should successfully update a driver")
    void Update_Success() {
        Driver driver = new Driver(1L, "Vinicius", "123456789", "88888888888");
        Driver newDriver = new Driver(1L, "Vinicius Henrique", "123456789", "88888888888");

        when(driverRepository.getReferenceById(1L)).thenReturn(driver);
        when(driverRepository.findByCnh("123456789")).thenReturn(Optional.of(driver));
        when(driverRepository.save(driver)).thenReturn(driver);

        Driver result = driverService.update(1L, newDriver);

        assertNotNull(result);
        assertEquals(driver, result);
        verify(driverRepository, times(1)).findByCnh("123456789");
        verify(driverRepository, times(1)).save(driver);
    }

    @Test
    @DisplayName("Should throw DataBaseException when trying to update a driver with duplicate CNH")
    void Update_Error_DriverDuplicate() {
        Driver otherDriver = new Driver(2L, "Maria", "123456787", "88888888888");
        when(driverRepository.findByCnh("123456787")).thenReturn(Optional.of(otherDriver));

        Driver newDriver = new Driver(1L, "Vinicius", "123456787", "88888888888");

        assertThrows(DataBaseException.class, () -> driverService.update(1L, newDriver));
        verify(driverRepository, times(1)).findByCnh("123456787");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating a non-existent driver")
    void Update_Error_DriverNotFound() {
        when(driverRepository.getReferenceById(1L)).thenThrow(EntityNotFoundException.class);

        Driver newDriver = new Driver(1L, "Vinicius", "123456787", "88888888888");

        assertThrows(ResourceNotFoundException.class, () -> driverService.update(1L, newDriver));
        verify(driverRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Should successfully delete a driver by ID")
    void Delete_Success() {
        driverService.delete(2L);

        verify(driverRepository, times(1)).deleteById(2L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to delete a non-existent driver")
    void Delete_Error_DriverNotFound() {
        doThrow(new EmptyResultDataAccessException(1)).when(driverRepository).deleteById(1L);

        assertThrows(ResourceNotFoundException.class, () -> driverService.delete(1L));
        verify(driverRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw DataBaseException when deleting a driver causes DataIntegrityViolationException")
    void Delete_Error_DataIntegrityViolationException() {
        doThrow(new DataIntegrityViolationException("error")).when(driverRepository).deleteById(1L);

        assertThrows(DataBaseException.class, () -> driverService.delete(1L));
        verify(driverRepository, times(1)).deleteById(1L);
    }


}
