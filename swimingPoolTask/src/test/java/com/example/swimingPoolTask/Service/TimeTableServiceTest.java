package com.example.swimingPoolTask.Service;
import com.example.swimingPoolTask.Entity.Order;
import com.example.swimingPoolTask.Entity.TimeTable;
import com.example.swimingPoolTask.Repository.OrderRepository;
import com.example.swimingPoolTask.Repository.TimeTableRepository;
import com.example.swimingPoolTask.Service.TimeTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeTableServiceTest {

    @Mock
    private TimeTableRepository timeTableRepository;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ClientService clientService;

    @InjectMocks
    private TimeTableService timeTableService;

    private TimeTable timeTable;
    private Order order;
    private LocalDateTime testTime;

    @BeforeEach
    void setUp() {
        testTime = LocalDateTime.of(2025, 4, 10, 10, 0);
        timeTable = new TimeTable(testTime, (byte) 10);
        order = new Order(1L, 1L, testTime);
    }

    @Test
    void findAll_ShouldReturnTimeTables() {
        when(timeTableRepository.findAll()).thenReturn(List.of(timeTable));

        List<TimeTable> result = timeTableService.findAll();

        assertEquals(1, result.size());
        verify(timeTableRepository, times(1)).findAll();
    }

    @Test
    void findByTime_ShouldReturnTimeTable() {
        when(timeTableRepository.findByTime(testTime)).thenReturn(Optional.of(timeTable));

        Optional<TimeTable> result = timeTableService.findByTime(testTime);

        assertTrue(result.isPresent());
        assertEquals(timeTable, result.get());
        verify(timeTableRepository, times(1)).findByTime(testTime);
    }

    @Test
    void save_ShouldCallRepositorySave() {
        timeTableService.save(timeTable);

        verify(timeTableRepository, times(1)).save(timeTable);
    }

    @Test
    void getAvailable_ShouldReturnAvailableSlots() {
        LocalDate testDate = testTime.toLocalDate();
        when(orderRepository.findAll()).thenReturn(List.of(order));

        Map<String, Integer> availableSlots = timeTableService.getAvailable(testDate);

        assertTrue(availableSlots.containsKey("10:00"));
        assertEquals(9, availableSlots.get("10:00"));
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void reserve_ShouldThrowExceptionWhenAlreadyBooked() {
        when(orderRepository.findAllByClientId(1L)).thenReturn(List.of(order));

        Exception exception = assertThrows(RuntimeException.class, () -> timeTableService.reserve(1L, testTime));
        assertEquals("Пользователь уже записан на этот день", exception.getMessage());
        verify(orderRepository, times(1)).findAllByClientId(1L);
    }

    @Test
    void reserve_ShouldThrowExceptionWhenNoAvailableSlots() {
        timeTable.setCount((byte) 0);
        when(timeTableRepository.findByTime(testTime)).thenReturn(Optional.of(timeTable));
        when(orderRepository.findAllByClientId(1L)).thenReturn(List.of());

        Exception exception = assertThrows(RuntimeException.class, () -> timeTableService.reserve(1L, testTime));
        assertEquals("Нет доступных мест", exception.getMessage());
        verify(timeTableRepository, times(1)).findByTime(testTime);
    }

    @Test
    void reserve_ShouldCreateOrderAndDecreaseCount() {
        when(clientService.checkClientExist(1L)).thenReturn(true);
        when(timeTableRepository.findByTime(testTime)).thenReturn(Optional.of(timeTable));
        when(orderRepository.findAllByClientId(1L)).thenReturn(List.of());
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        Long resultId = timeTableService.reserve(1L, testTime);

        // Assert
        assertNotNull(resultId);
        assertEquals(1L, resultId);
        assertEquals(9, timeTable.getCount()); // проверка уменьшения доступных мест

        verify(clientService, times(1)).checkClientExist(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(timeTableRepository, times(1)).save(timeTable);
    }

    @Test
    void cancel_ShouldDeleteOrderAndIncreaseSlotCount() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(timeTableRepository.findByTime(testTime)).thenReturn(Optional.of(timeTable));

        timeTableService.cancel(1L, 1L);

        verify(orderRepository, times(1)).delete(order);
        verify(timeTableRepository, times(1)).save(timeTable);
        assertEquals(10, timeTable.getCount());
    }
}
