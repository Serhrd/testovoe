package com.example.swimingPoolTask.Service;

import com.example.swimingPoolTask.Entity.Order;
import com.example.swimingPoolTask.Entity.TimeTable;
import com.example.swimingPoolTask.Repository.OrderRepository;
import com.example.swimingPoolTask.Repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimeTableService {
    TimeTableRepository timeTableRepository;
    OrderRepository orderRepository;

    public List<TimeTable> findAll() {
        return timeTableRepository.findAll();
    }

    public Optional<TimeTable> findByTime(LocalDateTime time) {
        return timeTableRepository.findByTime(time);
    }

    public void save(TimeTable timeTable) {
        timeTableRepository.save(timeTable);
    }

    public Map<String, Integer> getAvailable(LocalDate time) {
        List<Order> orders = orderRepository.findAll().stream()
                .filter(o -> o.getTime().toLocalDate().equals(time))
                .toList();

        Map<String, Integer> bookedSlots = orders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getTime().toLocalTime().toString(),
                        Collectors.summingInt(e -> 1)
                ));

        Map<String, Integer> availableSlots = new LinkedHashMap<>();
        for (int hour = 8; hour <= 20; hour++) {
            String timeSlot = LocalTime.of(hour, 0).toString();
            int available = 10 - bookedSlots.getOrDefault(timeSlot, 0);
            if (available > 0) {
                availableSlots.put(timeSlot, available);
            }
        }
        return availableSlots;
    }

    public Long reserve(Long clientId, LocalDateTime time) {

        TimeTable timeTable = this.findByTime(time)
                .orElseGet(() -> new TimeTable(time, (byte) 10));

        boolean alreadyBooked = orderRepository.findAllByClientId(clientId).stream()
                .anyMatch(o -> o.getClientId()
                        .equals(clientId));
        if (alreadyBooked) {
            throw new RuntimeException( "Пользователь уже записан на этот день");
        }
        if (timeTable.getCount() == 0) {
            throw new RuntimeException("Нет доступных мест");
        }

        Order order = new Order();
        order.setClientId(clientId);
        order.setTime(time);

        orderRepository.save(order);
        timeTable.setCount((byte) (timeTable.getCount() - 1));
        this.save(timeTable);

        log.info("Добавлена запись: {}", order);
        return order.getId();
    }

    public void cancel(Long clientId, Long orderId) {


        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Запись с ID " + orderId + " не найдена"));


        orderRepository.delete(order);

        TimeTable timeTable = this.findByTime(order.getTime()).orElse(null);
        if (timeTable != null) {
            timeTable.setCount((byte) (timeTable.getCount() + 1));
            this.save(timeTable);
        }

        log.info("Запись отменена: {}", order);

    }
}
