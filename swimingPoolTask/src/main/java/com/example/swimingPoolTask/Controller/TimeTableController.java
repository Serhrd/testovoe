package com.example.swimingPoolTask.Controller;

import com.example.swimingPoolTask.Entity.Order;
import com.example.swimingPoolTask.Entity.OrderDto;
import com.example.swimingPoolTask.Entity.TimeTable;
import com.example.swimingPoolTask.Repository.OrderRepository;
import com.example.swimingPoolTask.Repository.TimeTableRepository;
import com.example.swimingPoolTask.Service.TimeTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v0/pool/timetable")
@RequiredArgsConstructor
@Slf4j
public class TimeTableController {

    private final TimeTableService timeTableService;

    @GetMapping("/all")
    public ResponseEntity<Map<String, Integer>> getAllBookings(@RequestParam String date) {
        LocalDate selectedDate = LocalDate.parse(date);
        List<TimeTable> orders = timeTableService.findAll().stream()
                .filter(o -> o.getTime().toLocalDate().equals(selectedDate))
                .toList();

        Map<String, Integer> bookedSlots = orders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getTime().toLocalTime().toString(),
                        Collectors.summingInt(e -> 1)
                ));

        return ResponseEntity.ok(bookedSlots);
    }

    @GetMapping("/available")
    public ResponseEntity<Map<String, Integer>> getAvailableSlots(@RequestParam String date) {
        LocalDate selectedDate = LocalDate.parse(date);
        var availableSlots = timeTableService.getAvailable(selectedDate);
        return ResponseEntity.ok(availableSlots);
    }

    @PostMapping("/reserve")
    public ResponseEntity<Map<String, String>> reserve(@RequestBody Order request) {
        Long orderId = timeTableService.reserve(request.getClientId(),request.getTime());

        return ResponseEntity.ok(Collections.singletonMap("orderId",orderId.toString()));
    }


    // Отмена записи клиента
    @DeleteMapping("/cancel")
    public ResponseEntity<Map<String, String>> cancel(@RequestBody OrderDto request) {
        timeTableService.cancel(request.getClientId(),request.getOrderId());

        return ResponseEntity.ok(Collections.singletonMap("message", "Запись успешно отменена"));
    }
}
