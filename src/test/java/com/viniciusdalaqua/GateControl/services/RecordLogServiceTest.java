package com.viniciusdalaqua.GateControl.services;

import com.viniciusdalaqua.GateControl.entities.Driver;
import com.viniciusdalaqua.GateControl.entities.RecordLog;
import com.viniciusdalaqua.GateControl.entities.Vehicle;
import com.viniciusdalaqua.GateControl.entities.enuns.RecordType;
import com.viniciusdalaqua.GateControl.repositories.RecordLogRepository;
import com.viniciusdalaqua.GateControl.services.exception.DataBaseException;
import com.viniciusdalaqua.GateControl.services.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordLogServiceTest {

    @InjectMocks
    private RecordLogService recordLogService;

    @Mock
    private RecordLogRepository recordLogRepository;

    private Driver createDriver() {
        return new Driver(1L, "DriverTest", "000000000", "14999999999");
    }

    private Vehicle createVehicle(Driver driver) {
        return new Vehicle(1L, "abc-1234", "Civic", "Gray", driver);
    }

    private RecordLog createRecordLog(Driver driver, Vehicle vehicle) {
        return new RecordLog(1L, RecordType.IN, LocalDateTime.now(), vehicle, driver, "...");
    }

    @Test
    @DisplayName("Should get all records from database")
    void findAllSuccess() {
        Driver driver = createDriver();
        Vehicle vehicle = createVehicle(driver);
        RecordLog rl = createRecordLog(driver, vehicle);

        when(recordLogRepository.findAll()).thenReturn(Collections.singletonList(rl));
        List<RecordLog> recordLogs = recordLogService.findAll();

        assertNotNull(recordLogs);
        assertEquals(1, recordLogs.size());
        assertEquals(rl, recordLogs.get(0));
        verify(recordLogRepository, times(1)).findAll();
        verifyNoMoreInteractions(recordLogRepository);
    }

    @Test
    @DisplayName("Should get record by ID")
    void findByIdSuccess() {
        Driver driver = createDriver();
        Vehicle vehicle = createVehicle(driver);
        RecordLog rl = createRecordLog(driver, vehicle);

        when(recordLogRepository.findById(1L)).thenReturn(Optional.of(rl));
        RecordLog recordLog = recordLogService.findById(1L);

        assertNotNull(recordLog);
        assertEquals(rl, recordLog);
        verify(recordLogRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(recordLogRepository);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when record is not found")
    void findByIdError() {
        when(recordLogRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> recordLogService.findById(1L));
        assertEquals("Resource not found. id: 1", e.getMessage());
        verify(recordLogRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(recordLogRepository);
    }

    @Test
    @DisplayName("Should successfully insert a new record")
    void insert() {
        Driver driver = createDriver();
        Vehicle vehicle = createVehicle(driver);
        RecordLog rl = createRecordLog(driver, vehicle);

        when(recordLogRepository.save(any(RecordLog.class))).thenReturn(rl);

        RecordLog result = recordLogService.insert(rl);

        assertNotNull(result);
        assertEquals(rl, result);
        verify(recordLogRepository, times(1)).save(rl);
        verifyNoMoreInteractions(recordLogRepository);
    }

    @Test
    @DisplayName("Should update a record")
    void update() {
        Driver driver = createDriver();
        Vehicle vehicle = createVehicle(driver);
        RecordLog rlOld = createRecordLog(driver, vehicle);
        RecordLog rlNew = new RecordLog(1L, RecordType.OUT, LocalDateTime.now(), vehicle, driver, "...!");

        when(recordLogRepository.getReferenceById(1L)).thenReturn(rlOld);
        when(recordLogRepository.save(rlOld)).thenReturn(rlNew);

        RecordLog result = recordLogService.update(1L, rlNew);

        assertNotNull(result);
        assertEquals(rlNew.getRecordType(), result.getRecordType());
        verify(recordLogRepository, times(1)).getReferenceById(1L);
        verify(recordLogRepository, times(1)).save(rlOld);
        verifyNoMoreInteractions(recordLogRepository);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating a non-existent record")
    void updateErrorByNotFound() {
        RecordLog rlNew = new RecordLog();

        when(recordLogRepository.getReferenceById(1L)).thenThrow(new ResourceNotFoundException("Resource not found. id: 1"));

        assertThrows(ResourceNotFoundException.class, () -> recordLogService.update(1L, rlNew));
        verify(recordLogRepository, times(1)).getReferenceById(1L);
        verifyNoMoreInteractions(recordLogRepository);
    }

    @Test
    @DisplayName("Should successfully delete a record by ID")
    void deleteSuccess() {
        doNothing().when(recordLogRepository).deleteById(1L);

        assertDoesNotThrow(() -> recordLogService.delete(1L));
        verify(recordLogRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(recordLogRepository);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to delete a non-existent record")
    void deleteErrorRecordNotFound() {
        doThrow(new EmptyResultDataAccessException(1)).when(recordLogRepository).deleteById(1L);

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> recordLogService.delete(1L));
        assertEquals("Resource not found. id: 1", e.getMessage());
        verify(recordLogRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(recordLogRepository);
    }

    @Test
    @DisplayName("Should throw DataBaseException when deleting a record causes DataIntegrityViolationException")
    void deleteErrorDataIntegrityViolation() {
        doThrow(new DataIntegrityViolationException("error")).when(recordLogRepository).deleteById(1L);

        assertThrows(DataBaseException.class, () -> recordLogService.delete(1L));
        verify(recordLogRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(recordLogRepository);
    }
}
